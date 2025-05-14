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
@Table(name = "buying_prices")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BuyingPrice extends BaseEntity {
    @Id
    @Column(name = "buying_price_id", columnDefinition = "UUID DEFAULT gen_random_uuid()")
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID buyingPriceId;

    @Column(updatable = false)
    @NotNull(message = "Buying price value is required")
    BigDecimal buyingPriceValue;

    @Column(updatable = false)
    BigDecimal buyingPriceFluctuation;

    Boolean isCurrent;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    Product product;

    @PrePersist
    public void prePersist() {
        if (isCurrent == null) {
            isCurrent = true;
        }
        if (buyingPriceFluctuation == null) {
            buyingPriceFluctuation = BigDecimal.ZERO;
        }
    }

}
