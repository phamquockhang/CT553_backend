package com.pqkhang.ct553_backend.domain.booking.cart.mapper;

import com.pqkhang.ct553_backend.domain.booking.cart.dto.CartDetailDTO;
import com.pqkhang.ct553_backend.domain.booking.cart.entity.CartDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartDetailMapper {
    @Mapping(source = "productId", target = "product.productId")
    CartDetail toCartDetail(CartDetailDTO cartDetailDTO);

    @Mapping(source = "product.productId", target = "productId")
    CartDetailDTO toCartDetailDTO(CartDetail cartDetail);
}
