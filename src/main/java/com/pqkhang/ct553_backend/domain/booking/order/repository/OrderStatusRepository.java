package com.pqkhang.ct553_backend.domain.booking.order.repository;

import com.pqkhang.ct553_backend.domain.booking.order.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderStatusRepository extends JpaRepository<OrderStatus, Integer>, JpaSpecificationExecutor<OrderStatus> {
    List<OrderStatus> findAllByOrder_OrderId(String orderId);
}
