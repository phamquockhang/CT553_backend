package com.pqkhang.ct553_backend.domain.booking.product;

import com.pqkhang.ct553_backend.domain.booking.product.product_image.ProductImageDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

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

    Boolean isActivated;

    @NotNull(message = "Item ID is required")
    Integer itemId;

    List<ProductImageDTO> productImages;

    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
