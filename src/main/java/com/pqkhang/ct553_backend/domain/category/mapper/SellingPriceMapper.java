package com.pqkhang.ct553_backend.domain.category.mapper;

import com.pqkhang.ct553_backend.domain.category.dto.SellingPriceDTO;
import com.pqkhang.ct553_backend.domain.category.entity.SellingPrice;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SellingPriceMapper {
    SellingPriceDTO toSellingPriceDTO(SellingPrice sellingPrice);

    SellingPrice toSellingPrice(SellingPriceDTO sellingPriceDTO);
}
