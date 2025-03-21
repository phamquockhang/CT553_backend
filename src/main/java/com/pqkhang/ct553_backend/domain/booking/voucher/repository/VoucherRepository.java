package com.pqkhang.ct553_backend.domain.booking.voucher.repository;

import com.pqkhang.ct553_backend.domain.booking.voucher.entity.Voucher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Integer>, JpaSpecificationExecutor<Voucher> {
//    List<Voucher> findByStartDateBeforeAndStatus(LocalDate date, VoucherStatusEnum status);
//
//    List<Voucher> findByEndDateBeforeAndStatus(LocalDate date, VoucherStatusEnum status);

    @Modifying
    @Query("UPDATE Voucher v SET v.status = 'EXPIRED' WHERE v.endDate < :today AND v.status = 'INACTIVE'")
    int updateStatusByEndDate(LocalDate today);

    @Modifying
    @Query("UPDATE Voucher v SET v.status = 'INACTIVE' WHERE v.startDate < :today AND v.status = 'ACTIVE'")
    int updateStatusByStartDate(LocalDate today);

    Optional<Voucher> findByVoucherCode(String voucherCode);

    @Query("SELECT v FROM Voucher v WHERE v.status = 'ACTIVE' AND v.usedCount < v.usageLimit")
    Page<Voucher> findAllValidVouchers(Pageable pageable);
}
