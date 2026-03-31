package com.payflow.wallet.application.provided;

import java.math.BigDecimal;

public interface ChargeWallet {

    WalletResponse charge(ChargeRequest request);

    record ChargeRequest(Long memberId, BigDecimal amount) {
    }

    record WalletResponse(Long walletId, BigDecimal balance) {
    }
}
