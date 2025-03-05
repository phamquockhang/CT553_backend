package com.pqkhang.ct553_backend.domain.booking.order.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderStatusDTO {
    String orderStatusId;

    String status;

    LocalDateTime createdAt;
}
