package com.pqkhang.ct553_backend.domain.transaction.controller;

import com.pqkhang.ct553_backend.app.exception.ResourceNotFoundException;
import com.pqkhang.ct553_backend.app.response.ApiResponse;
import com.pqkhang.ct553_backend.app.response.Page;
import com.pqkhang.ct553_backend.domain.transaction.dto.TransactionDTO;
import com.pqkhang.ct553_backend.domain.transaction.dto.request.VNPayCallbackRequest;
import com.pqkhang.ct553_backend.domain.transaction.service.TransactionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TransactionController {
    TransactionService transactionService;
    Environment env;

    @GetMapping("/{id}")
    public ApiResponse<TransactionDTO> getTransactionById(@PathVariable String id) throws ResourceNotFoundException {
        return ApiResponse.<TransactionDTO>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .payload(transactionService.getTransactionById(id))
                .message("Lấy thông tin giao dịch thành công")
                .build();
    }

    @GetMapping
    public ApiResponse<Page<TransactionDTO>> getTransactions(@RequestParam Map<String, String> params) throws ResourceNotFoundException {
        return ApiResponse.<Page<TransactionDTO>>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .payload(transactionService.getAllTransactions(params))
                .message("Lấy danh sách giao dịch thành công")
                .build();
    }

    @PostMapping
    public ApiResponse<TransactionDTO> createTransaction(HttpServletRequest request, @RequestBody TransactionDTO transactionDTO) throws ResourceNotFoundException {
        return ApiResponse.<TransactionDTO>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .payload(transactionService.createTransaction(request, transactionDTO))
                .message("Tạo giao dịch thành công")
                .build();
    }

    @GetMapping("/vn-pay-callback")
    public ApiResponse<TransactionDTO> payCallbackHandler(HttpServletRequest request, HttpServletResponse response) throws Exception {
        VNPayCallbackRequest callbackRequest = new VNPayCallbackRequest();
        callbackRequest.setVnp_ResponseCode(request.getParameter("vnp_ResponseCode"));
        callbackRequest.setVnp_TransactionNo(request.getParameter("vnp_TransactionNo"));
        callbackRequest.setVnp_Amount(request.getParameter("vnp_Amount"));
        callbackRequest.setVnp_BankCode(request.getParameter("vnp_BankCode"));
        callbackRequest.setVnp_OrderInfo(request.getParameter("vnp_OrderInfo"));
        callbackRequest.setVnp_PayDate(request.getParameter("vnp_PayDate"));
        callbackRequest.setVnp_TxnRef(request.getParameter("vnp_TxnRef"));

        final String CLIENT_URL = env.getProperty("CLIENT_URL");
        TransactionDTO transactionDTO = transactionService.handleVNPayCallback(callbackRequest);
        if (callbackRequest.getVnp_ResponseCode().equals("00"))
            response.sendRedirect(CLIENT_URL + "/order/payment/success?transactionId=" + transactionDTO.getTransactionId());
        else
            response.sendRedirect(CLIENT_URL + "/order/payment/fail?transactionId=" + transactionDTO.getTransactionId());

        return ApiResponse.<TransactionDTO>builder()
                .status(HttpStatus.OK.value())
                .success(true)
                .payload(transactionDTO)
                .message("Xử lý callback VNPay thành công")
                .build();
    }

}
