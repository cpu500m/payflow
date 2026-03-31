package com.payflow.wallet.application.provided;

import java.math.BigDecimal;

public interface DeductBalance {

    DeductResult deduct(Long memberId, BigDecimal amount);

    enum DeductResult {
        SUCCESS,
        INSUFFICIENT_BALANCE,
        WALLET_NOT_FOUND
    }
}
