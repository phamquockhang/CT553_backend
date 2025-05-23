package com.pqkhang.ct553_backend.domain.category.repository;

import com.pqkhang.ct553_backend.domain.category.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, Integer>, JpaSpecificationExecutor<ProductImage> {
    List<ProductImage> findAllByProduct_ProductId(Integer productProductId);

    boolean existsByPublicId(String publicId);
}
