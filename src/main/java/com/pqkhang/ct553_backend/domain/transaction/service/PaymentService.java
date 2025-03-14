package com.pqkhang.ct553_backend.domain.transaction.service;

import com.pqkhang.ct553_backend.app.exception.ResourceNotFoundException;
import com.pqkhang.ct553_backend.domain.transaction.dto.response.VNPayResponse;
import com.pqkhang.ct553_backend.domain.transaction.entity.Transaction;
import jakarta.servlet.http.HttpServletRequest;

public interface PaymentService {
    VNPayResponse createVnPayPayment(HttpServletRequest request, Transaction transaction) throws ResourceNotFoundException;
}
