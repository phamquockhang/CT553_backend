package com.pqkhang.ct553_backend.domain.category.mapper;

import com.pqkhang.ct553_backend.domain.category.dto.ProductImageDTO;
import com.pqkhang.ct553_backend.domain.category.entity.ProductImage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductImageMapper {
    @Mapping(target = "productId", source = "product.productId")
    ProductImageDTO toProductImageDTO(ProductImage productImage);

    @Mapping(target = "product.productId", source = "productId")
    ProductImage toProductImage(ProductImageDTO productImageDTO);

}
