package com.pqkhang.ct553_backend.domain.booking.order.repository;

import com.pqkhang.ct553_backend.domain.booking.order.entity.SellingOrder;
import com.pqkhang.ct553_backend.domain.booking.order.enums.PaymentStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface SellingOrderRepository extends JpaRepository<SellingOrder, String>, JpaSpecificationExecutor<SellingOrder> {
    List<SellingOrder> findAllByCustomer_CustomerId(UUID customerId);

    // update expired transactions status to EXPIRED
    @Modifying
    @Query("UPDATE SellingOrder s SET s.paymentStatus = 'EXPIRED' WHERE s.paymentStatus = 'PENDING' AND s.createdAt < :minusTime")
    int updateExpiredSellingOrders(LocalDateTime minusTime);

    List<SellingOrder> findByPaymentStatusAndCreatedAtBefore(PaymentStatusEnum paymentStatus, LocalDateTime createdAtBefore);
}
