package com.pqkhang.ct553_backend.domain.booking.order.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderDetailDTO {
    Integer orderDetailId;

    Integer productId;

    Double quantity;

    BigDecimal price;
}
