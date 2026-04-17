package com.payflow.wallet.domain;

import com.payflow.wallet.domain.enums.TransactionType;

/**
 * @description    :
 */
public class WalletFixture {

	public static WalletRegisterRequest createWalletRegisterRequest() {
		return new WalletRegisterRequest(1L);
	}

	public static Wallet createWallet() {
		return Wallet.create(createWalletRegisterRequest());
	}

	public static Wallet createWalletWithBalance(long balance) {
		Wallet wallet = createWallet();
		if (balance > 0) {
			wallet.charge(balance);
		}
		return wallet;
	}

	public static WalletTransactionCreateRequest createTransactionRequest(TransactionType type, long amount) {
		return new WalletTransactionCreateRequest(type, amount, type.name() + " 거래");
	}

	public static WalletTransactionCreateRequest createTransactionRequest(
		TransactionType type, long amount, String description) {
		return new WalletTransactionCreateRequest(type, amount, description);
	}
}
