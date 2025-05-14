package com.pqkhang.ct553_backend.domain.category.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SellingPriceDTO {
    UUID sellingPriceId;

    @NotNull(message = "Selling price value is required")
    BigDecimal sellingPriceValue;

    BigDecimal sellingPriceFluctuation;

    Boolean isCurrent;

    LocalDateTime createdAt;
}
