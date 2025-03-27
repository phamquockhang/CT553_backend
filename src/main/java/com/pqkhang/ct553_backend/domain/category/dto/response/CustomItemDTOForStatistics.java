package com.pqkhang.ct553_backend.domain.category.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomItemDTOForStatistics {
    Integer itemId;

    String itemName;

    List<CustomProductDTOForStatistics> products;
}
