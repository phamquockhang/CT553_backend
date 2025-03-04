package com.pqkhang.ct553_backend.domain.booking.order.entity;

import com.pqkhang.ct553_backend.domain.booking.order.enums.StatusEnum;
import com.pqkhang.ct553_backend.domain.common.entity.BaseEntity;
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
@Table(name = "orders")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Order extends BaseEntity {
    @Id
    String orderId;

    @Enumerated(EnumType.STRING)
    StatusEnum status;

    BigDecimal totalAmount;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    Customer customer;

    @OneToMany(mappedBy = "order", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    List<OrderDetail> orderDetails;

}
