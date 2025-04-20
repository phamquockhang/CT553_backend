package com.pqkhang.ct553_backend.domain.user.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StaffStatisticDTO {
    String staffName;

    Integer processedOrders;

    Integer delayedOrders;
}
