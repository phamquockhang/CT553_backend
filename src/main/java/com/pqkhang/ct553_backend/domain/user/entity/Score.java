package com.pqkhang.ct553_backend.domain.user.entity;

import com.pqkhang.ct553_backend.domain.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "scores")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Score extends BaseEntity {

    @Id
    @Column(name = "score_id", columnDefinition = "UUID DEFAULT gen_random_uuid()")
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID scoreId;

    @Column(nullable = false)
    Integer changeAmount;

    @Column(updatable = false)
    Integer newValue;

    Boolean isCurrent;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    Customer customer;

    @PrePersist
    public void prePersist() {
        if (isCurrent == null) {
            isCurrent = true;
        }
    }

}
