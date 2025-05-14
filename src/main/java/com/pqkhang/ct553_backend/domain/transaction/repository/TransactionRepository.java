package com.pqkhang.ct553_backend.domain.transaction.repository;

import com.pqkhang.ct553_backend.domain.transaction.entity.Transaction;
import com.pqkhang.ct553_backend.domain.transaction.enums.TransactionStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, String>, JpaSpecificationExecutor<Transaction> {
//    // update expired transactions status to EXPIRED
//    @Modifying
//    @Query("UPDATE Transaction t SET t.status = 'EXPIRED' WHERE t.status = 'PENDING' AND t.createdAt < :minusTime")
//    int updateExpiredTransactions(LocalDateTime minusTime);

    List<Transaction> findByStatusAndCreatedAtBefore(TransactionStatusEnum transactionStatusEnum, LocalDateTime minusTime);
}
