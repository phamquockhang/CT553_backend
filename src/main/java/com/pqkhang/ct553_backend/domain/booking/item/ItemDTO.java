package com.pqkhang.ct553_backend.domain.booking.item;

import com.pqkhang.ct553_backend.domain.booking.product.ProductDTO;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemDTO {
    Integer itemId;

    @NotBlank(message = "Name item is required")
    String name;

    List<ProductDTO> products;

    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
