package com.pqkhang.ct553_backend.domain.category.entity;

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

    String productUnit;
    String description;
    Boolean isActivated;

    //    @ManyToOne(cascade = CascadeType.PERSIST)
    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    Item item;

    @OneToMany(mappedBy = "product", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    List<ProductImage> productImages;

    @OneToMany(mappedBy = "product", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    List<BuyingPrice> buyingPrices;

    @OneToMany(mappedBy = "product", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    List<SellingPrice> sellingPrices;

    @PrePersist
    public void prePersist() {
        if (isActivated == null) {
            isActivated = true;
        }
    }

}
