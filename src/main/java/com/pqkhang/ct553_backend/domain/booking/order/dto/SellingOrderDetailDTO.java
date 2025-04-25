package com.pqkhang.ct553_backend.domain.booking.order.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SellingOrderDetailDTO {
    Integer sellingOrderDetailId;

    Integer productId;

    String productName;

    String unit;

    Double quantity;

    Double totalWeight;

    BigDecimal unitPrice;

    BigDecimal totalPrice;
}
