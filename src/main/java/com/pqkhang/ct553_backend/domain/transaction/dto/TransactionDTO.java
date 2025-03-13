package com.pqkhang.ct553_backend.domain.transaction.dto;

import com.pqkhang.ct553_backend.domain.booking.order.dto.SellingOrderDTO;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TransactionDTO {
    Integer transactionId;

    @NotBlank(message = "Selling order id is required")
    SellingOrderDTO sellingOrder;

    @NotBlank(message = "Payment method is required")
    PaymentMethodDTO paymentMethod;

    String transactionType;

    @NotBlank(message = "Status is required")
    String status;

    BigDecimal amount;

    String txnRef;

    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
