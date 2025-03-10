package com.pqkhang.ct553_backend.domain.transaction.entity;

import com.pqkhang.ct553_backend.domain.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "payment_methods")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentMethod extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "payment_method_id_seq")
    @SequenceGenerator(name = "payment_method_id_seq", sequenceName = "payment_methods_seq", allocationSize = 1)
    Integer paymentMethodId;

    String paymentMethodName;

    @OneToMany(mappedBy = "paymentMethod", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    List<Transaction> transactions;
}
