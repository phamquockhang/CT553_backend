package com.pqkhang.ct553_backend.domain.transaction.utils;

import com.pqkhang.ct553_backend.domain.transaction.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TransactionScheduler {
    @Autowired
    private TransactionService transactionService;

    //    @Scheduled(cron = " */10 * * * * *")
    @Scheduled(cron = "0 */1 * * * *")
    public void checkExpiredOrders() {
        System.out.println("Checking for expired orders and transactions");
        transactionService.checkAndUpdateExpiredTransactions();
    }
}
