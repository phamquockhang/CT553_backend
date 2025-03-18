package com.pqkhang.ct553_backend.domain.user.dto;

import java.math.BigDecimal;

public class ScoreCalculator {
    private static final BigDecimal MONEY_UNIT = new BigDecimal("10000"); // 10.000 VNĐ
    private static final BigDecimal SCORE_PER_UNIT = new BigDecimal("100"); // 100 điểm ~ tương ứng 100 VNĐ khi quy đổi

    public static int convertMoneyToScores(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Số tiền không hợp lệ");
        }

        return amount.divideToIntegralValue(MONEY_UNIT).multiply(SCORE_PER_UNIT).intValueExact();
    }
}
