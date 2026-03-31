package com.payflow.wallet.domain;

import java.math.BigDecimal;

public class InsufficientBalanceException extends RuntimeException {

    public InsufficientBalanceException(BigDecimal currentBalance, BigDecimal requestedAmount) {
        super("Insufficient balance: current=%s, requested=%s".formatted(currentBalance, requestedAmount));
    }
}
