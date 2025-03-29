package com.pqkhang.ct553_backend.domain.notification.repository;

import com.pqkhang.ct553_backend.domain.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, UUID>, JpaSpecificationExecutor<Notification> {
    Optional<Notification> findBySellingOrder_SellingOrderId(String sellingOrderSellingOrderId);
}
