package com.pqkhang.ct553_backend.domain.booking.item;

import com.pqkhang.ct553_backend.domain.booking.product.ProductMapper;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {ProductMapper.class})
public interface ItemMapper {
    ItemDTO toItemDTO(Item Item);

    Item toItem(ItemDTO ItemDTO);

    void updateItemFromDTO(ItemDTO itemDTO, @MappingTarget Item item);
}
