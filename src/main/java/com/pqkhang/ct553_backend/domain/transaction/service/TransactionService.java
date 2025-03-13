package com.pqkhang.ct553_backend.domain.transaction.service;

import com.pqkhang.ct553_backend.app.exception.ResourceNotFoundException;
import com.pqkhang.ct553_backend.app.response.Page;
import com.pqkhang.ct553_backend.domain.transaction.dto.TransactionDTO;
import com.pqkhang.ct553_backend.domain.transaction.dto.request.VNPayCallbackRequest;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;

public interface TransactionService {
    TransactionDTO createTransaction(HttpServletRequest request, TransactionDTO transactionDTO) throws ResourceNotFoundException;

    TransactionDTO handleVNPayCallback(VNPayCallbackRequest request) throws Exception;

    TransactionDTO getTransactionById(Integer transactionId) throws ResourceNotFoundException;

    Page<TransactionDTO> getAllTransactions(Map<String, String> params) throws ResourceNotFoundException;

    void checkAndUpdateExpiredTransactions();
}
