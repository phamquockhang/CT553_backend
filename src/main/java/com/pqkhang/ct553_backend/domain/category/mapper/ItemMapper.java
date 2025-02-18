package com.pqkhang.ct553_backend.domain.category.mapper;

import com.pqkhang.ct553_backend.domain.category.dto.GeneralizedItemDTO;
import com.pqkhang.ct553_backend.domain.category.dto.ItemDTO;
import com.pqkhang.ct553_backend.domain.category.entity.Item;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {ProductMapper.class})
public interface ItemMapper {
    ItemDTO toItemDTO(Item Item);

    Item toItem(ItemDTO ItemDTO);

    void updateItemFromDTO(ItemDTO itemDTO, @MappingTarget Item item);

    GeneralizedItemDTO toGeneralizedItemDTO(Item item);
}
