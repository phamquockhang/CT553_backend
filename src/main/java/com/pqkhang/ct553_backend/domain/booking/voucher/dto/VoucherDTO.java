package com.pqkhang.ct553_backend.domain.booking.voucher.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VoucherDTO {
    Integer voucherId;

    String voucherCode;

    String status;

    @NotBlank(message = "Discount type is required")
    String discountType;

    @NotNull(message = "Discount value is required")
    BigDecimal discountValue;

    @NotNull(message = "Min order value is required")
    BigDecimal minOrderValue;

    BigDecimal maxDiscount;

    @NotNull(message = "Start date is required")
    LocalDate startDate;

    //    @NotNull(message = "End date is required")
    LocalDate endDate;

    @NotNull(message = "Usage limit is required")
    Integer usageLimit;

    Integer usedCount;

    List<UsedVoucherDTO> usedVoucher;

    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
