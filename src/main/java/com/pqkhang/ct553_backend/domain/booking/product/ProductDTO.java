package com.pqkhang.ct553_backend.domain.booking.product;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductDTO {
    Integer productId;

    @NotBlank(message = "Product name is required")
    String name;

    String description;

    Integer itemId;

    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
