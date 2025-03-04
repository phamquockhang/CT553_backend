package com.pqkhang.ct553_backend.domain.booking.order.mapper;

import com.pqkhang.ct553_backend.domain.booking.order.dto.OrderDTO;
import com.pqkhang.ct553_backend.domain.booking.order.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {OrderDetailMapper.class})
public interface OrderMapper {
    @Mapping(source = "customer.customerId", target = "customerId")
    OrderDTO toOrderDTO(Order order);

    @Mapping(source = "customerId", target = "customer.customerId")
    Order toOrder(OrderDTO orderDTO);
}
