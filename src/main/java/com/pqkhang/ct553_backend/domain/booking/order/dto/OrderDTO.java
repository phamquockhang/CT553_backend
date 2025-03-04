package com.pqkhang.ct553_backend.domain.booking.order.dto;

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

    String status;

    BigDecimal totalAmount;

    String customerId;

    List<OrderDetailDTO> orderDetails;

    LocalDateTime createdAt;
}
