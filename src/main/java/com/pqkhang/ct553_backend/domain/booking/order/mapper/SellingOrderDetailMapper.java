package com.pqkhang.ct553_backend.domain.booking.order.mapper;

import com.pqkhang.ct553_backend.domain.booking.order.dto.SellingOrderDetailDTO;
import com.pqkhang.ct553_backend.domain.booking.order.entity.SellingOrderDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SellingOrderDetailMapper {
    @Mapping(source = "productId", target = "product.productId")
    SellingOrderDetail toSellingOrderDetail(SellingOrderDetailDTO sellingOrderDetailDTO);

    @Mapping(source = "product.productId", target = "productId")
    SellingOrderDetailDTO toSellingOrderDetailDTO(SellingOrderDetail sellingOrderDetail);
}
