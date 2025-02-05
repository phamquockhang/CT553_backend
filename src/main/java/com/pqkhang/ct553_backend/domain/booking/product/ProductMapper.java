package com.pqkhang.ct553_backend.domain.booking.product;

import com.pqkhang.ct553_backend.domain.booking.product.product_image.ProductImageMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {ProductImageMapper.class})
public interface ProductMapper {
    ProductDTO toProductDTO(Product product);

    @Mapping(target = "item.itemId", source = "itemId")
    Product toProduct(ProductDTO productDTO);

    void updateProductFromDTO(ProductDTO productDTO, @MappingTarget Product product);
}
