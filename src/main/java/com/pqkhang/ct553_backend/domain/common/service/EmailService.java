package com.pqkhang.ct553_backend.domain.common.service;

import com.pqkhang.ct553_backend.domain.booking.order.entity.SellingOrder;
import com.pqkhang.ct553_backend.domain.transaction.entity.Transaction;

public interface EmailService {
    void sendSuccessfullyTransactionEmail(Transaction transaction);

    void sendSellingOrderStatusEmail(SellingOrder sellingOrder);
}
