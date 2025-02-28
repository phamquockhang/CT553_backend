package com.pqkhang.ct553_backend.domain.user.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ScoreDTO {
    UUID scoreId;

    @NotNull(message = "Change amount is required")
    Integer changeAmount;

    Integer newValue;

    Boolean isCurrent;

//    CustomerDTO customer;

    LocalDateTime createdAt;
}
