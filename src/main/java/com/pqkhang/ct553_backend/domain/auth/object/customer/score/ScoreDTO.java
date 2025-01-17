package com.pqkhang.ct553_backend.domain.auth.object.customer.score;

import com.pqkhang.ct553_backend.domain.auth.object.customer.CustomerDTO;
import jakarta.validation.constraints.NotBlank;
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

    @NotBlank(message = "Change amount is required")
    Integer changeAmount;

    @NotBlank(message = "New value is required")
    Integer newValue;

    Boolean isCurrent;

//    CustomerDTO customer;

    LocalDateTime createdAt;
}
