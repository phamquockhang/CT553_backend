package com.pqkhang.ct553_backend.domain.booking.voucher.repository;

import com.pqkhang.ct553_backend.domain.booking.voucher.entity.UsedVoucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface UsedVoucherRepository extends JpaRepository<UsedVoucher, Integer>, JpaSpecificationExecutor<UsedVoucher> {
}
