package com.payflow.wallet.application.provided;

import com.payflow.wallet.application.provided.ChargeWallet.WalletResponse;

public interface QueryBalance {

    WalletResponse getBalance(Long memberId);
}
