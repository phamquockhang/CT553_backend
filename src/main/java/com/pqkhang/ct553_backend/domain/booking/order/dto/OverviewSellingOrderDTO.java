package com.pqkhang.ct553_backend.domain.booking.order.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OverviewSellingOrderDTO {
    String sellingOrderId;

    String customerName;

    @NotBlank(message = "Order status is required")
    String orderStatus;
}
