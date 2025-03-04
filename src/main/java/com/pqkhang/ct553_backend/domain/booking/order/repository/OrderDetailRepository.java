package com.pqkhang.ct553_backend.domain.booking.order.repository;

import com.pqkhang.ct553_backend.domain.booking.order.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer>, JpaSpecificationExecutor<OrderDetail> {
    List<OrderDetail> findAllByOrder_OrderId(String orderId);
}
