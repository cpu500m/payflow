package com.payflow.payment.application.required;

import java.math.BigDecimal;

public interface WalletClient {

    DeductResult deduct(Long memberId, BigDecimal amount);

    void refund(Long memberId, BigDecimal amount);

    enum DeductResult {
        SUCCESS,
        INSUFFICIENT_BALANCE,
        WALLET_NOT_FOUND
    }
}
