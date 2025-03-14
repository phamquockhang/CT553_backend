package com.pqkhang.ct553_backend.infrastructure.service;

import com.pqkhang.ct553_backend.app.exception.ResourceNotFoundException;
import com.pqkhang.ct553_backend.domain.booking.order.entity.SellingOrder;
import com.pqkhang.ct553_backend.domain.booking.order.repository.SellingOrderRepository;
import com.pqkhang.ct553_backend.domain.transaction.dto.response.VNPayResponse;
import com.pqkhang.ct553_backend.domain.transaction.entity.Transaction;
import com.pqkhang.ct553_backend.domain.transaction.service.PaymentService;
import com.pqkhang.ct553_backend.infrastructure.config.payment.VNPayConfig;
import com.pqkhang.ct553_backend.infrastructure.utils.VNPayUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentServiceImpl implements PaymentService {

    SellingOrderRepository sellingOrderRepository;
    VNPayConfig vnPayConfig;

    @Override
    public VNPayResponse createVnPayPayment(HttpServletRequest request, Transaction transaction) throws ResourceNotFoundException {
        SellingOrder sellingOrder = sellingOrderRepository.findById(transaction.getSellingOrder().getSellingOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Selling order not found"));

        long amount = sellingOrder.getTotalAmount().multiply(BigDecimal.valueOf(100L)).longValue();
        Map<String, String> vnpParamsMap = vnPayConfig.getVNPayConfig(sellingOrder.getSellingOrderId());
        vnpParamsMap.put("vnp_Amount", String.valueOf(amount));
        vnpParamsMap.put("vnp_IpAddr", VNPayUtils.getIpAddress(request));
        vnpParamsMap.put("vnp_TxnRef", transaction.getTransactionId());
        String queryUrl = VNPayUtils.getPaymentURL(vnpParamsMap, true);
        String hashData = VNPayUtils.getPaymentURL(vnpParamsMap, false);
        String vnpSecureHash = VNPayUtils.hmacSHA512(vnPayConfig.getSecretKey(), hashData);
        queryUrl += "&vnp_SecureHash=" + vnpSecureHash;
        String paymentUrl = vnPayConfig.getVnp_PayUrl() + "?" + queryUrl;
        return VNPayResponse.builder()
                .paymentUrl(paymentUrl)
                .build();
    }
}