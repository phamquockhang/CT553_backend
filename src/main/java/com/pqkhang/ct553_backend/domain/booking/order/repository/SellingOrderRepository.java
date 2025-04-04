package com.pqkhang.ct553_backend.domain.booking.order.repository;

import com.pqkhang.ct553_backend.domain.booking.order.entity.SellingOrder;
import com.pqkhang.ct553_backend.domain.booking.order.enums.PaymentStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface SellingOrderRepository extends JpaRepository<SellingOrder, String>, JpaSpecificationExecutor<SellingOrder> {
    List<SellingOrder> findAllByCustomer_CustomerId(UUID customerId);

    List<SellingOrder> findByPaymentStatusAndCreatedAtBefore(PaymentStatusEnum paymentStatus, LocalDateTime createdAtBefore);

    List<SellingOrder> findAllByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    //    @Query("SELECT s.assignedStaffEmail, COUNT(s.sellingOrderId) FROM SellingOrder s WHERE DATE(s.createdAt) = CURRENT_DATE GROUP BY s.assignedStaffEmail")
    @Query(value = "SELECT s.assigned_staff_email, COUNT(s.selling_order_id) " +
            "FROM selling_orders s " +
            "WHERE DATE(s.created_at) = CURRENT_DATE " +
            "GROUP BY s.assigned_staff_email", nativeQuery = true)
    List<Object[]> countOrdersByAssignedStaffToday();
}
