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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PACKAGE, makeFinal = true)
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
    public List<VoucherDTO> getAllValidVouchers(BigDecimal totalAmount) {
        List<Voucher> validVouchers = voucherRepository.findAll((root, query, cb) -> {
            Predicate minOrderPredicate = cb.lessThanOrEqualTo(root.get("minOrderValue"), totalAmount);
            Predicate statusPredicate = cb.equal(root.get("status"), VoucherStatusEnum.ACTIVE);
            return cb.and(minOrderPredicate, statusPredicate);
        });

        return validVouchers.stream()
                .map(this::toOverViewVoucherDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void createVoucher(VoucherDTO voucherDTO) {
//        OrderStatusEnum orderStatusEnum = OrderStatusEnum.valueOf(sellingOrderDTO.getOrderStatus());
        Voucher voucher = voucherMapper.toVoucher(voucherDTO);

        voucher.setVoucherCode(VoucherUtils.generateVoucherCode());
        voucher.setStatus(VoucherStatusEnum.INACTIVE);

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
    public void useVoucher(Integer voucherId) throws ResourceNotFoundException {
        Voucher voucher = voucherRepository.findById(voucherId).orElseThrow(() -> new ResourceNotFoundException("Voucher not found"));

        voucher.setUsedCount(voucher.getUsedCount() + 1);
        voucherRepository.save(voucher);
    }
}
