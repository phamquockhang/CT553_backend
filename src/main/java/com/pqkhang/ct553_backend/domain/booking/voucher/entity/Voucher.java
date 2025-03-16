package com.pqkhang.ct553_backend.domain.booking.voucher.entity;

import com.pqkhang.ct553_backend.domain.booking.voucher.enums.DiscountTypeEnum;
import com.pqkhang.ct553_backend.domain.booking.voucher.enums.VoucherStatusEnum;
import com.pqkhang.ct553_backend.domain.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "vouchers")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Voucher extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "voucher_id_seq")
    @SequenceGenerator(name = "voucher_id_seq", sequenceName = "vouchers_seq", allocationSize = 1)
    Integer voucherId;

    @Column(unique = true, nullable = false, updatable = false, length = 10)
    String voucherCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    VoucherStatusEnum status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    DiscountTypeEnum discountType;

    @Column(nullable = false)
    BigDecimal discountValue;

    @Column(nullable = false)
    BigDecimal minOrderValue;

    BigDecimal maxDiscount;

    @Column(nullable = false, updatable = false)
    LocalDate startDate;

    @Column(nullable = false, updatable = false)
    LocalDate endDate;

    @Column(nullable = false)
    Integer usageLimit;

    @Column(nullable = false)
    Integer usedCount;
}
