package com.pqkhang.ct553_backend.domain.category.repository;

import com.pqkhang.ct553_backend.domain.category.entity.BuyingPrice;
import com.pqkhang.ct553_backend.domain.category.entity.Weight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WeightRepository extends JpaRepository<Weight, Integer>, JpaSpecificationExecutor<Weight> {
    List<Weight> findAllByProduct_ProductId(Integer productId);

    Weight findByProduct_ProductIdAndIsCurrent(Integer productId, boolean b);
}
