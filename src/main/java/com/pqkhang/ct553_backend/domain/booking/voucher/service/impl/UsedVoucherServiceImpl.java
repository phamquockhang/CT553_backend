package com.pqkhang.ct553_backend.domain.booking.voucher.service.impl;

import com.pqkhang.ct553_backend.app.exception.ResourceNotFoundException;
import com.pqkhang.ct553_backend.app.response.Meta;
import com.pqkhang.ct553_backend.app.response.Page;
import com.pqkhang.ct553_backend.domain.booking.order.entity.SellingOrder;
import com.pqkhang.ct553_backend.domain.booking.order.repository.SellingOrderRepository;
import com.pqkhang.ct553_backend.domain.booking.voucher.dto.UsedVoucherDTO;
import com.pqkhang.ct553_backend.domain.booking.voucher.entity.UsedVoucher;
import com.pqkhang.ct553_backend.domain.booking.voucher.entity.Voucher;
import com.pqkhang.ct553_backend.domain.booking.voucher.mapper.UsedVoucherMapper;
import com.pqkhang.ct553_backend.domain.booking.voucher.repository.UsedVoucherRepository;
import com.pqkhang.ct553_backend.domain.booking.voucher.repository.VoucherRepository;
import com.pqkhang.ct553_backend.domain.booking.voucher.service.UsedVoucherService;
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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PACKAGE, makeFinal = true)
@Slf4j
public class UsedVoucherServiceImpl implements UsedVoucherService {

    RequestParamUtils requestParamUtils;
    StringUtils stringUtils;
    UsedVoucherMapper usedVoucherMapper;
    UsedVoucherRepository usedVoucherRepository;

    static String DEFAULT_PAGE = "1";
    static String DEFAULT_PAGE_SIZE = "10";
    private final VoucherRepository voucherRepository;
    private final SellingOrderRepository sellingOrderRepository;

    private Pageable createPageable(Map<String, String> params) {
        int page = Integer.parseInt(params.getOrDefault("page", DEFAULT_PAGE));
        int pageSize = Integer.parseInt(params.getOrDefault("pageSize", DEFAULT_PAGE_SIZE));
        List<Sort.Order> sortOrders = requestParamUtils.toSortOrders(params, UsedVoucher.class);
        return PageRequest.of(page - 1, pageSize, Sort.by(sortOrders));
    }

    private Specification<UsedVoucher> buildSearchSpec(Map<String, String> params) {
        return Specification.where(buildQuerySpec_VoucherCode(params));
    }

    private Specification<UsedVoucher> buildQuerySpec_VoucherCode(Map<String, String> params) {
        return params.containsKey("query") ? (root, query, criteriaBuilder) -> {
            String[] searchValues = params.get("query").trim().toLowerCase().split(",");
            Predicate[] predicates = Arrays.stream(searchValues)
                    .map(stringUtils::normalizeString)
                    .map(value -> "%" + value.trim() + "%")
                    .map(pattern -> criteriaBuilder.or(
                            criteriaBuilder.like(criteriaBuilder.function("unaccent", String.class, criteriaBuilder.lower(root.get("voucher").get("voucherCode"))), pattern),
                            criteriaBuilder.like(criteriaBuilder.function("unaccent", String.class, criteriaBuilder.lower(root.get("sellingOrder").get("sellingOrderId"))), pattern)
                    ))
                    .toArray(Predicate[]::new);
            return criteriaBuilder.or(predicates);
        } : null;
    }

    private Page<UsedVoucherDTO> buildUsedVoucherPage(org.springframework.data.domain.Page<UsedVoucher> usedVoucherPage, Pageable pageable) throws ResourceNotFoundException {
        if (usedVoucherPage.isEmpty()) {
            throw new ResourceNotFoundException("No used voucher found");
        }
        Meta meta = Meta.builder()
                .page(pageable.getPageNumber() + 1)
                .pageSize(pageable.getPageSize())
                .pages(usedVoucherPage.getTotalPages())
                .total(usedVoucherPage.getTotalElements())
                .build();
        return Page.<UsedVoucherDTO>builder()
                .meta(meta)
                .data(usedVoucherPage.getContent()
                        .stream().map(usedVoucherMapper::toUsedVoucherDTO)
                        .collect(Collectors.toList())
                )
                .build();
    }

    @Override
    public UsedVoucherDTO getUsedVoucher(Integer usedVoucherId) throws ResourceNotFoundException {
        UsedVoucher usedVoucher = usedVoucherRepository.findById(usedVoucherId)
                .orElseThrow(() -> new ResourceNotFoundException("Used voucher not found"));
        return usedVoucherMapper.toUsedVoucherDTO(usedVoucher);
    }

    @Override
    public Page<UsedVoucherDTO> getUsedVouchers(Map<String, String> params) throws ResourceNotFoundException {
        Pageable pageable = createPageable(params);
        Specification<UsedVoucher> searchSpec = buildSearchSpec(params);
        return buildUsedVoucherPage(usedVoucherRepository.findAll(searchSpec, pageable), pageable);
    }

//    @Transactional
    @Override
    public void createUsedVoucher(UsedVoucherDTO usedVoucherDTO) {
        log.info("1. usedVoucherDTO: {}", usedVoucherDTO);

        UsedVoucher usedVoucher = new UsedVoucher();
        Voucher voucher = voucherRepository.findByVoucherCode(usedVoucherDTO.getVoucherCode()).orElseThrow();
        usedVoucher.setVoucher(voucher);
        SellingOrder sellingOrder = sellingOrderRepository.findById(usedVoucherDTO.getSellingOrderId()).orElseThrow();
        usedVoucher.setSellingOrder(sellingOrder);
        usedVoucher.setDiscountAmount(usedVoucherDTO.getDiscountAmount());

        log.info("2. Create used voucher: {}", usedVoucher);
        usedVoucherRepository.save(usedVoucher);
        log.info("3. Create used voucher: {}", usedVoucher);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void deleteUsedVoucher(Integer usedVoucherId) throws ResourceNotFoundException {
        if (!usedVoucherRepository.existsById(usedVoucherId)) {
            throw new ResourceNotFoundException("Used voucher not found");
        }

        usedVoucherRepository.deleteById(usedVoucherId);
    }
}
