package com.pqkhang.ct553_backend.domain.booking.order.repository;

import com.pqkhang.ct553_backend.domain.booking.order.entity.SellingOrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<SellingOrderDetail, Integer>, JpaSpecificationExecutor<SellingOrderDetail> {
    List<SellingOrderDetail> findAllBySellingOrder_SellingOrderId(String orderId);
}
