package com.pqkhang.ct553_backend.domain.booking.voucher.entity;

import com.pqkhang.ct553_backend.domain.booking.order.entity.SellingOrder;
import com.pqkhang.ct553_backend.domain.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "used_vouchers")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UsedVoucher extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "used_voucher_id_seq")
    @SequenceGenerator(name = "used_voucher_id_seq", sequenceName = "used_vouchers_seq", allocationSize = 1)
    Integer usedVoucherId;

    @ManyToOne
    @JoinColumn(name = "voucher_id", nullable = false)
    Voucher voucher;

    @OneToOne
    @JoinColumn(name = "selling_order_id", nullable = false)
    SellingOrder sellingOrder;

    @Column(nullable = false, updatable = false)
    BigDecimal discountAmount;
}
