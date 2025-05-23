package com.pqkhang.ct553_backend.domain.category.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductImageDTO {
    Integer productImageId;

    String publicId;

    String imageUrl;

    @NotNull(message = "Product ID is required")
    Integer productId;

    LocalDateTime createdAt;
}
