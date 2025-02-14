package com.pqkhang.ct553_backend.domain.category.entity;

import com.pqkhang.ct553_backend.domain.common.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "selling_prices")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SellingPrice extends BaseEntity {
    @Id
    @Column(name = "selling_price_id", columnDefinition = "UUID DEFAULT gen_random_uuid()")
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID sellingPriceId;

    @Column(updatable = false)
    @NotNull(message = "Selling price value is required")
    BigDecimal sellingPriceValue;

    @Column(updatable = false)
    BigDecimal sellingPriceFluctuation;

    Boolean isCurrent;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    Product product;

    @PrePersist
    public void prePersist() {
        if (isCurrent == null) {
            isCurrent = true;
        }
        if (sellingPriceFluctuation == null) {
            sellingPriceFluctuation = BigDecimal.ZERO;
        }
    }

}
