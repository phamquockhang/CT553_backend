package com.pqkhang.ct553_backend.domain.transaction.service.impl;

import com.pqkhang.ct553_backend.app.exception.ResourceNotFoundException;
import com.pqkhang.ct553_backend.app.response.Meta;
import com.pqkhang.ct553_backend.app.response.Page;
import com.pqkhang.ct553_backend.domain.transaction.dto.PaymentMethodDTO;
import com.pqkhang.ct553_backend.domain.transaction.entity.PaymentMethod;
import com.pqkhang.ct553_backend.domain.transaction.mapper.PaymentMethodMapper;
import com.pqkhang.ct553_backend.domain.transaction.repository.PaymentMethodRepository;
import com.pqkhang.ct553_backend.domain.transaction.service.PaymentMethodService;
import com.pqkhang.ct553_backend.infrastructure.utils.RequestParamUtils;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class PaymentMethodServiceImpl implements PaymentMethodService {
    PaymentMethodRepository paymentMethodRepository;
    PaymentMethodMapper paymentMethodMapper;
    RequestParamUtils requestParamUtils;

    @Override
    public PaymentMethodDTO createPaymentMethod(PaymentMethodDTO paymentMethodDTO) {
        PaymentMethod paymentMethod = paymentMethodMapper.toPaymentMethod(paymentMethodDTO);
        return paymentMethodMapper.toPaymentMethodDTO(paymentMethodRepository.save(paymentMethod));
    }

    @Override
    public PaymentMethodDTO updatePaymentMethod(Integer id, PaymentMethodDTO paymentMethodDTO) throws ResourceNotFoundException {
        PaymentMethod paymentMethod = paymentMethodRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Payment method not found"));
        paymentMethodMapper.updatePaymentMethodFromDTO(paymentMethod, paymentMethodDTO);
        return paymentMethodMapper.toPaymentMethodDTO(paymentMethodRepository.save(paymentMethod));
    }

    @Override
    public void deletePaymentMethod(Integer id) throws ResourceNotFoundException {
        PaymentMethod paymentMethod = paymentMethodRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Payment method not found"));
        paymentMethodRepository.delete(paymentMethod);
    }

    @Override
    public Page<PaymentMethodDTO> getPaymentMethods(Map<String, String> params) {
        int page = Integer.parseInt(params.getOrDefault("page", "1"));
        int pageSize = Integer.parseInt(params.getOrDefault("pageSize", "10"));
        List<Sort.Order> sortOrders = requestParamUtils.toSortOrders(params, PaymentMethod.class);
        Pageable pageable = PageRequest.of(page - 1, pageSize, Sort.by(sortOrders));
        org.springframework.data.domain.Page<PaymentMethod> paymentMethodPage = paymentMethodRepository.findAll(pageable);
        Meta meta = Meta.builder()
                .page(pageable.getPageNumber() + 1)
                .pageSize(pageable.getPageSize())
                .pages(paymentMethodPage.getTotalPages())
                .total(paymentMethodPage.getTotalElements())
                .build();
        return Page.<PaymentMethodDTO>builder()
                .meta(meta)
                .data(paymentMethodPage.getContent().stream()
                        .map(paymentMethodMapper::toPaymentMethodDTO)
                        .collect(Collectors.toList()))
                .build();
    }

    @Override
    public List<PaymentMethodDTO> getAllPaymentMethods() {
        return paymentMethodRepository.findAll().stream()
                .map(paymentMethodMapper::toPaymentMethodDTO)
                .collect(Collectors.toList());
    }
}
