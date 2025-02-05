package com.pqkhang.ct553_backend.domain.booking.product.product_image;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductImageMapper {
    @Mapping(target = "productId", source = "product.productId")
    ProductImageDTO toProductImageDTO(ProductImage productImage);

    //    @Mapping(target = "product.productId", source = "productId")
    ProductImage toProductImage(ProductImageDTO productImageDTO);

//    void updateProductImageFromDTO(ProductImageDTO productImageDTO, @MappingTarget ProductImage productImage);
}
