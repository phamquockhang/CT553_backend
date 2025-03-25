package com.pqkhang.ct553_backend.domain.booking.order.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SellingOrderStatisticsDTO {
    BigDecimal totalRevenue;

    Integer totalNewOrders;

    Integer totalDeliveringOrders;

    Integer totalDeliveredOrders;

    Integer totalCancelledOrders;
}
