package com.pqkhang.ct553_backend.domain.booking.product.product_image;

import jakarta.validation.constraints.NotBlank;
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

    @NotBlank(message = "Product image URL is required")
    String imageUrl;

    Integer productId;

    LocalDateTime createdAt;
}
