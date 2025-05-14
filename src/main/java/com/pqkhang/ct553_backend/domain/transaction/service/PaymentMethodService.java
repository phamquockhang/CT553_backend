package com.pqkhang.ct553_backend.domain.transaction.service;


import com.pqkhang.ct553_backend.app.exception.ResourceNotFoundException;
import com.pqkhang.ct553_backend.app.response.Page;
import com.pqkhang.ct553_backend.domain.transaction.dto.PaymentMethodDTO;

import java.util.List;
import java.util.Map;

public interface PaymentMethodService {
    PaymentMethodDTO createPaymentMethod(PaymentMethodDTO paymentMethodDTO);

    PaymentMethodDTO updatePaymentMethod(Integer id, PaymentMethodDTO paymentMethodDTO) throws ResourceNotFoundException;

    void deletePaymentMethod(Integer id) throws ResourceNotFoundException;

    Page<PaymentMethodDTO> getPaymentMethods(Map<String, String> params);

    List<PaymentMethodDTO> getAllPaymentMethods();
}
