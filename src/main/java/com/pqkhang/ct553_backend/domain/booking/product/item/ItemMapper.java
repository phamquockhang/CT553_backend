package com.pqkhang.ct553_backend.domain.booking.product.item;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ItemMapper {
    ItemDTO toItemDTO(Item Item);

    Item toItem(ItemDTO ItemDTO);

    void updateItemFromDTO(ItemDTO itemDTO, @MappingTarget Item item);
}
