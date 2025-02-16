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
@Table(name = "weights")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Weight extends BaseEntity {
    @Id
    @Column(name = "weight_id", columnDefinition = "UUID DEFAULT gen_random_uuid()")
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID weightId;

    @Column(updatable = false)
    @NotNull(message = "Weight value is required")
    BigDecimal weightValue;

    Boolean isCurrent;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    Product product;

    @PrePersist
    public void prePersist() {
        if (isCurrent == null) {
            isCurrent = true;
        }
    }

}
