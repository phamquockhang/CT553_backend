package com.pqkhang.ct553_backend.domain.booking.cart.repository;

import com.pqkhang.ct553_backend.domain.booking.cart.entity.CartDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartDetailRepository extends JpaRepository<CartDetail, Integer>, JpaSpecificationExecutor<CartDetail> {

    List<CartDetail> findAllByCart_CartId(Integer cartCartId);
}
