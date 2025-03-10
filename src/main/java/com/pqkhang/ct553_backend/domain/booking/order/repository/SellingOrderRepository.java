package com.pqkhang.ct553_backend.domain.booking.order.repository;

import com.pqkhang.ct553_backend.domain.booking.order.entity.SellingOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SellingOrderRepository extends JpaRepository<SellingOrder, String>, JpaSpecificationExecutor<SellingOrder> {
    List<SellingOrder> findAllByCustomer_CustomerId(UUID customerId);
}
