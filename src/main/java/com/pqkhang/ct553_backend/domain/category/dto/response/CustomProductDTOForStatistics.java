package com.pqkhang.ct553_backend.domain.category.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomProductDTOForStatistics {
    Integer productId;

    String productName;

    String remainingQuantity;
}
