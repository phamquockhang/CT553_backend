package com.pqkhang.ct553_backend.domain.booking.order.entity;

import com.pqkhang.ct553_backend.domain.category.entity.Product;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "order_details")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_detail_id_seq")
    @SequenceGenerator(name = "order_detail_id_seq", sequenceName = "order_details_seq", allocationSize = 1)
    Integer orderDetailId;

    Double quantity;

    BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    Order order;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    Product product;
}
