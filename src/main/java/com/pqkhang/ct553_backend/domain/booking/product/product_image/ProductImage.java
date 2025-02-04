package com.pqkhang.ct553_backend.domain.booking.product.product_image;

import com.pqkhang.ct553_backend.domain.booking.product.Product;
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
@Table(name = "product_images")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductImage extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_image_id_seq")
    @SequenceGenerator(name = "product_image_id_seq", sequenceName = "product_images_seq", allocationSize = 1)
    Integer productImageId;

    @Column(nullable = false)
    String imageUrl;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    Product product;
}
