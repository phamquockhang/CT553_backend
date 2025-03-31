package com.pqkhang.ct553_backend.domain.booking.order.entity;

import com.pqkhang.ct553_backend.domain.booking.order.enums.OrderStatusEnum;
import com.pqkhang.ct553_backend.domain.booking.order.enums.PaymentStatusEnum;
import com.pqkhang.ct553_backend.domain.booking.voucher.entity.UsedVoucher;
import com.pqkhang.ct553_backend.domain.common.entity.BaseEntity;
import com.pqkhang.ct553_backend.domain.notification.entity.Notification;
import com.pqkhang.ct553_backend.domain.user.entity.Customer;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "selling_orders")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SellingOrder extends BaseEntity {
    @Id
    String sellingOrderId;

    String customerName;

    String phone;

    String email;

    String address;

    String note;

    BigDecimal totalAmount;

    Integer usedScore;

    Integer earnedScore;

    @Enumerated(EnumType.STRING)
    PaymentStatusEnum paymentStatus;

    @OneToMany(mappedBy = "sellingOrder", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    List<OrderStatus> orderStatuses;

    @Enumerated(EnumType.STRING)
    OrderStatusEnum orderStatus;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    Customer customer;

    String assignedStaffEmail;

    @OneToMany(mappedBy = "sellingOrder", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    List<SellingOrderDetail> sellingOrderDetails;

    @OneToOne(mappedBy = "sellingOrder", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    UsedVoucher usedVoucher;

    @OneToMany(mappedBy = "sellingOrder", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    List<Notification> notifications;
}
