package com.pqkhang.ct553_backend.domain.booking.order.mapper;

import com.pqkhang.ct553_backend.domain.booking.order.dto.OrderStatusDTO;
import com.pqkhang.ct553_backend.domain.booking.order.entity.OrderStatus;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderStatusMapper {
    OrderStatus toOrderStatus(OrderStatusDTO orderStatusDTO);

    OrderStatusDTO toOrderStatusDTO(OrderStatus orderStatus);
}
