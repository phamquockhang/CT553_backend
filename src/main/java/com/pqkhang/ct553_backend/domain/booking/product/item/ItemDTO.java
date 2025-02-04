package com.pqkhang.ct553_backend.domain.booking.product.item;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemDTO {
    Integer itemId;

    @NotBlank(message = "Name item is required")
    String name;

    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
