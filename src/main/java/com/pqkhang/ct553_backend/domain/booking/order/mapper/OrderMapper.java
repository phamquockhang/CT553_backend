package com.pqkhang.ct553_backend.domain.booking.order.mapper;

import com.pqkhang.ct553_backend.domain.booking.order.dto.OrderDTO;
import com.pqkhang.ct553_backend.domain.booking.order.entity.Order;
import com.pqkhang.ct553_backend.domain.booking.order.dto.OrderDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring"
//        , uses = {OrderDetailMapper.class}
)
public interface OrderMapper {
    OrderDTO toOrderDTO(Order order);

    Order toOrder(OrderDTO orderDTO);
}
