package com.pqkhang.ct553_backend.domain.booking.order.repository;

import com.pqkhang.ct553_backend.domain.booking.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, String>, JpaSpecificationExecutor<Order> {

    List<Order> findAllByCustomer_CustomerId(UUID customerId);
}
