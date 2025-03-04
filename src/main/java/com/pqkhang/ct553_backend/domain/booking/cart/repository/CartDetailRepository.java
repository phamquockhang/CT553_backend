package com.pqkhang.ct553_backend.domain.booking.cart.repository;

import com.pqkhang.ct553_backend.domain.booking.cart.entity.CartDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartDetailRepository extends JpaRepository<CartDetail, Integer>, JpaSpecificationExecutor<CartDetail> {
    List<CartDetail> findAllByCart_CartId(Integer cartCartId);

    @Query("delete from CartDetail cd where cd.cartDetailId = :cartDetailId")
    @Modifying
    void customDeleteCartDetailById(@Param("cartDetailId") Integer cartDetailId);

    CartDetail findByCart_CartIdAndProduct_ProductId(int cartId, int productId);
}
