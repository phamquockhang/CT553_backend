package com.pqkhang.ct553_backend.domain.user.dto;

import java.math.BigDecimal;

public class ScoreCalculator {
    private static final BigDecimal MONEY_UNIT = new BigDecimal("100000"); // 100.000 VNĐ
    private static final BigDecimal SCORE_PER_UNIT = new BigDecimal("5000"); // 5000 điểm

    public static int convertMoneyToScores(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Số tiền không hợp lệ");
        }
        // Lấy phần nguyên của phép chia amount / 100000 rồi nhân với 5000
        return amount.divideToIntegralValue(MONEY_UNIT).multiply(SCORE_PER_UNIT).intValueExact();
    }
}
