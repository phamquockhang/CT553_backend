package com.pqkhang.ct553_backend.domain.transaction.repository;

import com.pqkhang.ct553_backend.domain.transaction.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface TransactionRepository extends JpaRepository<Transaction, String>, JpaSpecificationExecutor<Transaction> {
    // update expired transactions status to EXPIRED
    @Modifying
    @Query("UPDATE Transaction t SET t.status = 'EXPIRED' WHERE t.status = 'PENDING' AND t.createdAt < :minusTime")
    int updateExpiredTransactions(LocalDateTime minusTime);
}
