package com.payflow.wallet.application.provided;

import com.payflow.wallet.domain.Wallet;

/**
 * @description    :
 */
public interface WalletFinder {
	Wallet findByMemberId(Long memberId);

	Long getBalance(Long memberId);
}
