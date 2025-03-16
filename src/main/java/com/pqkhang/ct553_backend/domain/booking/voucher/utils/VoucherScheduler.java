package com.pqkhang.ct553_backend.domain.booking.voucher.utils;

import com.pqkhang.ct553_backend.domain.booking.voucher.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class VoucherScheduler {
    @Autowired
    private VoucherService voucherService;

    //    @Scheduled(cron = "0 0 0 * * *")
    @Scheduled(cron = " */10 * * * * *")
    public void checkExpiredVouchers() {
        System.out.println("🔄 Bắt đầu cập nhật trạng thái voucher vào 0h...");
        voucherService.updateVoucherStatusDaily();
        System.out.println("✅ Hoàn thành cập nhật trạng thái voucher.");
    }
}
