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
public class BuyingPriceDTO {
    UUID buyingPriceId;

    @NotNull(message = "Buying price value is required")
    BigDecimal buyingPriceValue;

    BigDecimal buyingPriceFluctuation;

    Boolean isCurrent;

    LocalDateTime createdAt;
}
