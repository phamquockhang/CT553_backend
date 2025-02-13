package com.pqkhang.ct553_backend.domain.category.mapper;

import com.pqkhang.ct553_backend.domain.category.dto.BuyingPriceDTO;
import com.pqkhang.ct553_backend.domain.category.entity.BuyingPrice;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BuyingPriceMapper {
    BuyingPriceDTO toBuyingPriceDTO(BuyingPrice buyingPrice);

    BuyingPrice toBuyingPrice(BuyingPriceDTO buyingPriceDTO);
}
