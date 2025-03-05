package com.pqkhang.ct553_backend.domain.booking.order.entity;

import com.pqkhang.ct553_backend.domain.booking.order.enums.OrderStatusEnum;
import com.pqkhang.ct553_backend.domain.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "order_statuses")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderStatus extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_status_id_seq")
    @SequenceGenerator(name = "order_status_id_seq", sequenceName = "order_statuses_seq", allocationSize = 1)
    Integer orderStatusId;

    @Enumerated(EnumType.STRING)
    OrderStatusEnum status;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    Order order;
}
