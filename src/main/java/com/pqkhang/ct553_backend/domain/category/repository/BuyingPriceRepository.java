package com.pqkhang.ct553_backend.domain.category.repository;

import com.pqkhang.ct553_backend.domain.category.entity.BuyingPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BuyingPriceRepository extends JpaRepository<BuyingPrice, Integer>, JpaSpecificationExecutor<BuyingPrice> {
    List<BuyingPrice> findAllByProduct_ProductId(Integer productId);

    BuyingPrice findByProduct_ProductIdAndIsCurrent(Integer productId, boolean b);
}
