package com.pqkhang.ct553_backend.domain.booking.order.dto.request;

import com.pqkhang.ct553_backend.domain.booking.order.dto.SellingOrderDTO;
import lombok.*;
import lombok.experimental.FieldDefaults;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestSellingOrderDTO extends SellingOrderDTO{
    String voucherCode;
}

