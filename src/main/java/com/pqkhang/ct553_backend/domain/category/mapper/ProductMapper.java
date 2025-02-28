package com.pqkhang.ct553_backend.domain.category.mapper;

import com.pqkhang.ct553_backend.domain.category.dto.ProductDTO;
import com.pqkhang.ct553_backend.domain.category.entity.Item;
import com.pqkhang.ct553_backend.domain.category.entity.Product;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {ProductImageMapper.class, BuyingPriceMapper.class, SellingPriceMapper.class})
public interface ProductMapper {
    @Mapping(target = "itemId", source = "item.itemId")
    ProductDTO toProductDTO(Product product);

    @Mapping(target = "item.itemId", source = "itemId")
    Product toProduct(ProductDTO productDTO);

    @BeforeMapping
    default void setItem(ProductDTO productDTO, @MappingTarget Product product) {
        if (productDTO.getItemId() != null){
           if(product.getItem() == null || !product.getItem().getItemId().equals(productDTO.getItemId())) {
               product.setItem(Item.builder().itemId(productDTO.getItemId()).build());
           }
        }
    }

    void updateProductFromDTO(ProductDTO productDTO, @MappingTarget Product product);
}
