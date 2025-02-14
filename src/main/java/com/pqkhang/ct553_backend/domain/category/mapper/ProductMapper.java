package com.pqkhang.ct553_backend.domain.category.mapper;

import com.pqkhang.ct553_backend.domain.category.dto.ProductDTO;
import com.pqkhang.ct553_backend.domain.category.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {ProductImageMapper.class, BuyingPriceMapper.class, SellingPriceMapper.class})
public interface ProductMapper {
    @Mapping(target = "itemId", source = "item.itemId")
    ProductDTO toProductDTO(Product product);

    @Mapping(target = "item.itemId", source = "itemId")
    Product toProduct(ProductDTO productDTO);

    void updateProductFromDTO(ProductDTO productDTO, @MappingTarget Product product);
}
