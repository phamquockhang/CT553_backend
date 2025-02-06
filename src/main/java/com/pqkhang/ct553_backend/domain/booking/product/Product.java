package com.pqkhang.ct553_backend.domain.booking.product;

import com.pqkhang.ct553_backend.domain.booking.item.Item;
import com.pqkhang.ct553_backend.domain.booking.product.product_image.ProductImage;
import com.pqkhang.ct553_backend.domain.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "products")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Product extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_id_seq")
    @SequenceGenerator(name = "product_id_seq", sequenceName = "products_seq", allocationSize = 1)
    Integer productId;

    @Getter
    String productName;
    
    String description;
    Boolean isActivated;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    Item item;

    @OneToMany(mappedBy = "product", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    List<ProductImage> productImages;

    @PrePersist
    public void prePersist() {
        if (isActivated == null) {
            isActivated = true;
        }
    }

}
