package com.payflow.merchant.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record CommissionRate(BigDecimal rate) {

    public CommissionRate {
        if (rate == null || rate.compareTo(BigDecimal.ZERO) < 0 || rate.compareTo(new BigDecimal("100")) > 0) {
            throw new IllegalArgumentException("수수료율은 0 ~ 100 사이여야 합니다. :" + rate);
        }
    }

    public BigDecimal calculate(BigDecimal amount) {
        return amount.multiply(rate).divide(new BigDecimal("100"), 0, RoundingMode.HALF_UP);
    }
}
