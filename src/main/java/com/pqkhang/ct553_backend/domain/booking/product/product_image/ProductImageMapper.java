package com.pqkhang.ct553_backend.domain.booking.product.product_image;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductImageMapper {
    ProductImageDTO toProductImageDTO(ProductImage productImage);

    //    @Mapping(target = "item.itemId", source = "itemId")
    ProductImage toProductImage(ProductImageDTO productImageDTO);

//    void updateProductImageFromDTO(ProductImageDTO productImageDTO, @MappingTarget ProductImage productImage);
}
