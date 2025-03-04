package com.pqkhang.ct553_backend.domain.booking.cart.mapper;

import com.pqkhang.ct553_backend.domain.booking.cart.dto.CartDTO;
import com.pqkhang.ct553_backend.domain.booking.cart.entity.Cart;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {CartDetailMapper.class})
public interface CartMapper {
    CartDTO toCartDTO(Cart cart);

    Cart toCart(CartDTO cartDTO);
}
