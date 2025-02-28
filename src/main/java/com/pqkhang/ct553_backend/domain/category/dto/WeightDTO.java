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
public class WeightDTO {
    UUID weightId;

    @NotNull(message = "Weight value is required")
    BigDecimal weightValue;

    Boolean isCurrent;

    LocalDateTime createdAt;
}
