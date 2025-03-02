package com.pqkhang.ct553_backend.domain.booking.cart.entity;

import com.pqkhang.ct553_backend.domain.category.entity.Product;
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
@Table(name = "cart_details")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartDetail extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cart_detail_id_seq")
    @SequenceGenerator(name = "cart_detail_id_seq", sequenceName = "cart_details_seq", allocationSize = 1)
    Integer cartDetailId;

    Double quantity;

    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false)
    Cart cart;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    Product product;
}
