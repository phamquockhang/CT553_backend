package com.pqkhang.ct553_backend.domain.booking.voucher.repository;

import com.pqkhang.ct553_backend.domain.booking.voucher.entity.Voucher;
import com.pqkhang.ct553_backend.domain.booking.voucher.enums.VoucherStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Integer>, JpaSpecificationExecutor<Voucher> {
    List<Voucher> findByStartDateBeforeAndStatus(LocalDate date, VoucherStatusEnum status);

    List<Voucher> findByEndDateBeforeAndStatus(LocalDate date, VoucherStatusEnum status);

    Optional<Voucher> findByVoucherCode(String voucherCode);
}
