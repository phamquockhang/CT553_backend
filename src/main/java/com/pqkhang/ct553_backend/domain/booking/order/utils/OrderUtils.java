package com.pqkhang.ct553_backend.domain.booking.order.utils;

import org.apache.commons.lang3.RandomStringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class OrderUtils {
    public static String generateOrderId() {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy");
        String formattedDate = currentDate.format(formatter);
        String randomCode = RandomStringUtils.secure().nextAlphanumeric(6).toUpperCase();
        return "KSEA-" + randomCode + "-" + formattedDate;
    }
}
