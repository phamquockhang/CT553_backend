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
public class OrderDTO {
    String orderId;

    @NotBlank(message = "Customer ID is required")
    String customerId;

    @NotBlank(message = "Name is required")
    String name;

    @NotBlank(message = "Phone is required")
    String phone;

    String email;

    @NotBlank(message = "Address is required")
    String address;

    String note;

    @NotNull(message = "Total amount is required")
    BigDecimal totalAmount;

    @NotBlank(message = "Payment status is required")
    String paymentStatus;

    String orderStatus;

    List<OrderStatusDTO> orderStatuses;

    @NotNull(message = "Order details is required")
    List<OrderDetailDTO> orderDetails;

    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
