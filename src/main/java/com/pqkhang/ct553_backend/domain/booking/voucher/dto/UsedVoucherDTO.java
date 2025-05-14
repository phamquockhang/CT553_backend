package com.pqkhang.ct553_backend.domain.booking.voucher.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UsedVoucherDTO {
    Integer usedVoucherId;

    @NotBlank(message = "Voucher code is required")
    String voucherCode;

    @NotBlank(message = "Selling order id is required")
    String sellingOrderId;

    @NotNull(message = "Discount amount is required")
    BigDecimal discountAmount;

    LocalDateTime createdAt;
}
