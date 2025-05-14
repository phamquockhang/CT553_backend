package com.pqkhang.ct553_backend.infrastructure.utils;

import com.pqkhang.ct553_backend.domain.booking.order.service.SellingOrderService;
import com.pqkhang.ct553_backend.domain.booking.voucher.service.VoucherService;
import com.pqkhang.ct553_backend.domain.transaction.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SchedulerUtils {
    @Autowired
    private VoucherService voucherService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private SellingOrderService sellingOrderService;

    //    @Scheduled(cron = " */10 * * * * *")
    @Scheduled(cron = "0 0 0 * * *")
    public void checkExpiredVouchers() {
        System.out.println("🔄 Voucher: Bắt đầu cập nhật trạng thái vào 0h...");
        voucherService.updateVoucherStatusDaily();
        System.out.println("✅ Voucher: Hoàn thành cập nhật trạng thái.");
    }

    //    @Scheduled(cron = " */10 * * * * *")
    @Scheduled(cron = "0 */1 * * * *")
    public void checkExpiredOrders() {
        System.out.println("🔄 Transaction: Bắt đầu cập nhật trạng thái.");
        transactionService.checkAndUpdateExpiredTransactions();
        System.out.println("✅ Transaction: Hoàn thành cập nhật trạng thái.");
    }

    @Scheduled(cron = "0 */30 * * * *")
    public void checkDelayedOrders() {
        System.out.println("🔄 SellingOrder: Bắt đầu kiểm tra các đơn hàng xử lí chậm.");
        sellingOrderService.checkDelayedOrders();
        System.out.println("✅ SellingOrder: Hoàn thành kiểm tra các đơn hàng xử lí chậm.");
    }
}
