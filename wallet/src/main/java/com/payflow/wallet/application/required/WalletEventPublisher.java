package com.payflow.wallet.application.required;

import com.payflow.wallet.domain.Wallet;

import java.math.BigDecimal;

public interface WalletEventPublisher {

    void publishCharged(Wallet wallet, BigDecimal amount);

    void publishDeducted(Wallet wallet, BigDecimal amount);
}
