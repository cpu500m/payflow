package com.payflow.wallet.application.provided;

import com.payflow.wallet.domain.Wallet;
import com.payflow.wallet.domain.WalletTransaction;
import com.payflow.wallet.domain.dto.WalletTransactionCreateRequest;

/**
 * @description    :
 */
public interface WalletTransactionModifier {
	WalletTransaction write(Wallet wallet, WalletTransactionCreateRequest createDto);
}
