package com.pqkhang.ct553_backend.domain.booking.order.mapper;

import com.pqkhang.ct553_backend.domain.booking.order.dto.OrderDetailDTO;
import com.pqkhang.ct553_backend.domain.booking.order.entity.OrderDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderDetailMapper {
    @Mapping(source = "productId", target = "product.productId")
    OrderDetail toOrderDetail(OrderDetailDTO orderDetailDTO);

    @Mapping(source = "product.productId", target = "productId")
    OrderDetailDTO toOrderDetailDTO(OrderDetail orderDetail);
}
