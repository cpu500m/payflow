package com.payflow.settlement.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record CommissionPolicy(BigDecimal rate) {

    private static final BigDecimal DEFAULT_RATE = new BigDecimal("0.035");

    public CommissionPolicy {
        if (rate == null || rate.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Commission rate must be non-negative");
        }
    }

    public static CommissionPolicy defaultPolicy() {
        return new CommissionPolicy(DEFAULT_RATE);
    }

    public BigDecimal calculate(BigDecimal amount) {
        if (amount == null) {
            throw new IllegalArgumentException("Amount must not be null");
        }
        return amount.multiply(rate).setScale(2, RoundingMode.HALF_UP);
    }
}
