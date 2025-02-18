package com.pqkhang.ct553_backend.domain.category.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GeneralizedItemDTO {
    Integer itemId;

    @NotBlank(message = "Name item is required")
    String itemName;

    Boolean isActivated;

    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
