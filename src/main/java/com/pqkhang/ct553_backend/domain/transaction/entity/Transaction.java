package com.pqkhang.ct553_backend.domain.transaction.entity;

import com.pqkhang.ct553_backend.domain.booking.order.entity.SellingOrder;
import com.pqkhang.ct553_backend.domain.common.entity.BaseEntity;
import com.pqkhang.ct553_backend.domain.transaction.enums.TransactionStatusEnum;
import com.pqkhang.ct553_backend.domain.transaction.enums.TransactionTypeEnum;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "transactions")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Transaction extends BaseEntity {
    @Id
    String transactionId;

    @Enumerated(EnumType.STRING)
    TransactionTypeEnum transactionType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="selling_order_id")
    SellingOrder sellingOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="payment_method_id")
    PaymentMethod paymentMethod;

    BigDecimal amount;

    @Enumerated(EnumType.STRING)
    TransactionStatusEnum status;

    @Version
    Integer version;
}
