package com.pqkhang.ct553_backend.domain.booking.voucher.service.impl;

import com.pqkhang.ct553_backend.app.exception.ResourceNotFoundException;
import com.pqkhang.ct553_backend.app.response.Meta;
import com.pqkhang.ct553_backend.app.response.Page;
import com.pqkhang.ct553_backend.domain.booking.voucher.dto.VoucherDTO;
import com.pqkhang.ct553_backend.domain.booking.voucher.entity.Voucher;
import com.pqkhang.ct553_backend.domain.booking.voucher.enums.DiscountTypeEnum;
import com.pqkhang.ct553_backend.domain.booking.voucher.enums.VoucherStatusEnum;
import com.pqkhang.ct553_backend.domain.booking.voucher.mapper.VoucherMapper;
import com.pqkhang.ct553_backend.domain.booking.voucher.repository.VoucherRepository;
import com.pqkhang.ct553_backend.domain.booking.voucher.service.VoucherService;
import com.pqkhang.ct553_backend.domain.booking.voucher.utils.VoucherUtils;
import com.pqkhang.ct553_backend.infrastructure.utils.RequestParamUtils;
import com.pqkhang.ct553_backend.infrastructure.utils.StringUtils;
import jakarta.persistence.criteria.Predicate;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PACKAGE, makeFinal = true)
@Slf4j
public class VoucherServiceImpl implements VoucherService {

    RequestParamUtils requestParamUtils;
    StringUtils stringUtils;
    VoucherMapper voucherMapper;
    VoucherRepository voucherRepository;

    static String DEFAULT_PAGE = "1";
    static String DEFAULT_PAGE_SIZE = "10";

    private Pageable createPageable(Map<String, String> params) {
        int page = Integer.parseInt(params.getOrDefault("page", DEFAULT_PAGE));
        int pageSize = Integer.parseInt(params.getOrDefault("pageSize", DEFAULT_PAGE_SIZE));
        List<Sort.Order> sortOrders = requestParamUtils.toSortOrders(params, Voucher.class);
        return PageRequest.of(page - 1, pageSize, Sort.by(sortOrders));
    }

    private Specification<Voucher> buildSearchSpec(Map<String, String> params) {
        return Specification.where(buildQuerySpec_VoucherCode(params)).and(buildVoucherStatusSpec(params)).and(buildDiscountTypeSpec(params));
    }

    private Specification<Voucher> buildQuerySpec_VoucherCode(Map<String, String> params) {
        return params.containsKey("query") ? (root, query, cb) -> {
            String[] searchValues = params.get("query").trim().toLowerCase().split(",");
            Predicate[] predicates = Arrays.stream(searchValues).map(stringUtils::normalizeString).map(value -> "%" + value.trim() + "%").map(pattern -> cb.like(cb.function("unaccent", String.class, cb.lower(root.get("voucherCode"))), pattern)).toArray(Predicate[]::new);
            return cb.or(predicates);
        } : null;
    }

    private Specification<Voucher> buildVoucherStatusSpec(Map<String, String> params) {
        return requestParamUtils.getSearchCriteria(params, "status").stream()
                .map(criteria -> (Specification<Voucher>) (root, query, cb) ->
                        cb.equal(root.get("status"), VoucherStatusEnum.valueOf(criteria.getValue().toString().toUpperCase()))
                )
                .reduce(Specification::or)
                .orElse(null);
    }

    private Specification<Voucher> buildDiscountTypeSpec(Map<String, String> params) {
        return requestParamUtils.getSearchCriteria(params, "discountType").stream()
                .map(criteria -> (Specification<Voucher>) (root, query, cb) ->
                        cb.equal(root.get("discountType"), DiscountTypeEnum.valueOf(criteria.getValue().toString().toUpperCase()))
                )
                .reduce(Specification::or)
                .orElse(null);
    }

    private Page<VoucherDTO> buildVoucherPage(org.springframework.data.domain.Page<Voucher> voucherPage, Pageable pageable) throws ResourceNotFoundException {
        if (voucherPage.isEmpty()) {
            throw new ResourceNotFoundException("No voucher found");
        }
        Meta meta = Meta.builder()
                .page(pageable.getPageNumber() + 1)
                .pageSize(pageable.getPageSize())
                .pages(voucherPage.getTotalPages())
                .total(voucherPage.getTotalElements())
                .build();
        return Page.<VoucherDTO>builder()
                .meta(meta)
                .data(voucherPage.getContent()
                        .stream().map(this::toOverViewVoucherDTO)
                        .collect(Collectors.toList())
                )
                .build();
    }

    private VoucherDTO toOverViewVoucherDTO(Voucher voucher) {
        VoucherDTO voucherDTO = voucherMapper.toVoucherDTO(voucher);

        // logic simplify here

        return voucherDTO;
    }

    @Override
    public VoucherDTO getVoucher(Integer voucherId) throws ResourceNotFoundException {
        Voucher voucher = voucherRepository.findById(voucherId).orElseThrow(() -> new ResourceNotFoundException("Voucher not found"));
        return voucherMapper.toVoucherDTO(voucher);
    }

    @Override
    public Page<VoucherDTO> getVouchers(Map<String, String> params) throws ResourceNotFoundException {
        Pageable pageable = createPageable(params);
        Specification<Voucher> spec = buildSearchSpec(params);
        return buildVoucherPage(voucherRepository.findAll(spec, pageable), pageable);
    }

    @Override
    public Page<VoucherDTO> getAllValidVouchers(Map<String, String> params) throws ResourceNotFoundException {
        Pageable pageable = createPageable(params);
        return buildVoucherPage(voucherRepository.findAllValidVouchers(pageable), pageable);
    }

    @Override
    @Transactional
    public void createVoucher(VoucherDTO voucherDTO) {
//        OrderStatusEnum orderStatusEnum = OrderStatusEnum.valueOf(sellingOrderDTO.getOrderStatus());
        Voucher voucher = voucherMapper.toVoucher(voucherDTO);

        voucher.setVoucherCode(VoucherUtils.generateVoucherCode());
        voucher.setStatus(VoucherStatusEnum.INACTIVE);
        voucher.setUsedCount(0);

        voucherRepository.save(voucher);
    }

    @Override
    @Transactional
    public void updateVoucher(Integer voucherId, VoucherDTO voucherDTO) throws ResourceNotFoundException {
        voucherRepository.findById(voucherId).orElseThrow(() -> new ResourceNotFoundException("Voucher not found"));
        Voucher voucher = voucherMapper.toVoucher(voucherDTO);
        voucherRepository.save(voucher);
    }

    @Override
    @Transactional
    public void deleteVoucher(Integer voucherId) throws ResourceNotFoundException {
        voucherRepository.findById(voucherId).orElseThrow(() -> new ResourceNotFoundException("Voucher not found"));
        voucherRepository.deleteById(voucherId);
    }

    @Override
    @Transactional
    public void useVoucher(String voucherCode) throws ResourceNotFoundException {
        Voucher voucher = voucherRepository.findByVoucherCode(voucherCode)
                .orElseThrow(() -> new ResourceNotFoundException("Voucher not found"));

        if (voucher.getUsedCount() >= voucher.getUsageLimit()) {
            throw new ResourceNotFoundException("Voucher has reached usage limit");
        }

        voucher.setUsedCount(voucher.getUsedCount() + 1);
        if (voucher.getUsedCount() >= voucher.getUsageLimit()) {
            voucher.setStatus(VoucherStatusEnum.OUT_OF_USES);
        }

        voucherRepository.save(voucher);
    }

    @Override
    @Transactional
    public void returnVoucher(String voucherCode) throws ResourceNotFoundException {
        Voucher voucher = voucherRepository.findByVoucherCode(voucherCode)
                .orElseThrow(() -> new ResourceNotFoundException("Voucher not found"));

        if (voucher.getUsedCount() > 0) {
            voucher.setUsedCount(voucher.getUsedCount() - 1);
        }

        if (voucher.getUsedCount() < voucher.getUsageLimit()) {
            voucher.setStatus(VoucherStatusEnum.ACTIVE);
        }

        voucherRepository.save(voucher);
    }

    @Override
    @Transactional
    public void updateVoucherStatusDaily() {
//        LocalDate today = LocalDate.now();
//
//        // Cập nhật voucher đang được kích hoạt thành hết hạn sử dụng
//        List<Voucher> expiredVouchers = voucherRepository.findByEndDateBeforeAndStatus(today, VoucherStatusEnum.ACTIVE);
//        expiredVouchers.forEach(voucher -> voucher.setStatus(VoucherStatusEnum.EXPIRED));
//        if (!expiredVouchers.isEmpty()) {
//            log.info("Đã cập nhật trạng thái hết hạn sử dụng cho {} voucher.", expiredVouchers.size());
//        }
//
//        // Cập nhật voucher chưa kích hoạt thành kích hoạt
//        List<Voucher> activeVouchers = voucherRepository.findByStartDateBeforeAndStatus(today, VoucherStatusEnum.INACTIVE);
//        activeVouchers.forEach(voucher -> voucher.setStatus(VoucherStatusEnum.ACTIVE));
//        if (!activeVouchers.isEmpty()) {
//            log.info("Đã cập nhật trạng thái kích hoạt cho {} voucher.", activeVouchers.size());
//        }
//
//        voucherRepository.saveAll(expiredVouchers);
//        voucherRepository.saveAll(activeVouchers);

        LocalDate today = LocalDate.now();

        int expiredCount = voucherRepository.updateStatusByEndDate(today);
        int activatedCount = voucherRepository.updateStatusByStartDate(today);

        if(expiredCount > 0) {
            log.info("Đã cập nhật trạng thái hết hạn sử dụng cho {} voucher.", expiredCount);
        }

        if(activatedCount > 0) {
            log.info("Đã cập nhật trạng thái kích hoạt cho {} voucher.", activatedCount);
        }
    }
}
