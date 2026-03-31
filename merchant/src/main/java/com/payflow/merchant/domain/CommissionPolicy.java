package com.payflow.merchant.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record CommissionPolicy(BigDecimal rate) {

    public CommissionPolicy {
        if (rate == null || rate.compareTo(BigDecimal.ZERO) < 0 || rate.compareTo(BigDecimal.ONE) > 0) {
            throw new IllegalArgumentException("Commission rate must be between 0 and 1: " + rate);
        }
    }

    public BigDecimal calculate(BigDecimal amount) {
        return amount.multiply(rate).setScale(0, RoundingMode.HALF_UP);
    }
}
