package com.pqkhang.ct553_backend.domain.booking.cart.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartDTO {
    Integer cartId;

    List<CartDetailDTO> cartDetails;

    LocalDateTime createdAt;
}
