package com.pqkhang.ct553_backend.domain.booking.order.dto;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderAndOrderDetailDTO {
    UUID customerId;

    OrderDTO order;

//    List<OrderDetailDTO> orderDetailDTOList;
}
