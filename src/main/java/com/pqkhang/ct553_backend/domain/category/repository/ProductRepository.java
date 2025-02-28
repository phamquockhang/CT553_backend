package com.pqkhang.ct553_backend.domain.category.repository;

import com.pqkhang.ct553_backend.domain.category.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer>, JpaSpecificationExecutor<Product> {
    boolean existsByProductName(String name);

    Product findProductByProductId(Integer productId);
}
