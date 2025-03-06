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
@Table(name = "selling_order_details")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SellingOrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "selling_order_detail_id_seq")
    @SequenceGenerator(name = "selling_order_detail_id_seq", sequenceName = "selling_order_details_seq", allocationSize = 1)
    Integer sellingOrderDetailId;

    String productName;

    String unit;

    Double quantity;

    BigDecimal unitPrice;

    BigDecimal totalPrice;

    @ManyToOne
    @JoinColumn(name = "selling_order_id", nullable = false)
    SellingOrder sellingOrder;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    Product product;
}
