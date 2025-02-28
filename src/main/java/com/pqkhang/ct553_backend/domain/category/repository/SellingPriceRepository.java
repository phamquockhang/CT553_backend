package com.pqkhang.ct553_backend.domain.category.repository;

import com.pqkhang.ct553_backend.domain.category.entity.SellingPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SellingPriceRepository extends JpaRepository<SellingPrice, Integer>, JpaSpecificationExecutor<SellingPrice> {
    List<SellingPrice> findAllByProduct_ProductId(Integer productId);

    SellingPrice findByProduct_ProductIdAndIsCurrent(Integer productId, boolean b);
}
