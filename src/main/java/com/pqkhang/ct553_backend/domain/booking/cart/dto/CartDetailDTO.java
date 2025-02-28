package com.pqkhang.ct553_backend.domain.booking.cart.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartDetailDTO {
    Integer cartDetailId;

    Integer quantity;

    Integer productId;

    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
