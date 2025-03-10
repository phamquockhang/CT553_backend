package com.pqkhang.ct553_backend.domain.booking.order.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SellingOrderDTO {
    String sellingOrderId;

    String customerId;

    String customerName;

    String phone;

    String email;

    String address;

    String note;

    BigDecimal totalAmount;

    Integer usedScore;

    Integer earnedScore;

    @NotBlank(message = "Payment status is required")
    String paymentStatus;

    @NotBlank(message = "Order status is required")
    String orderStatus;

    List<OrderStatusDTO> orderStatuses;

    @NotNull(message = "Order details is required")
    List<SellingOrderDetailDTO> sellingOrderDetails;

    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
