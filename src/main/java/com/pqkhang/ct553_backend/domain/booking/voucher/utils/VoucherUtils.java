package com.pqkhang.ct553_backend.domain.booking.voucher.utils;

import org.apache.commons.lang3.RandomStringUtils;

public class VoucherUtils {
    public static String generateVoucherCode() {
//        LocalDate currentDate = LocalDate.now();
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy");
//        String formattedDate = currentDate.format(formatter);
        String randomCode = RandomStringUtils.secure().nextAlphanumeric(5).toUpperCase();
        return "KSEA-" + randomCode;
    }
}