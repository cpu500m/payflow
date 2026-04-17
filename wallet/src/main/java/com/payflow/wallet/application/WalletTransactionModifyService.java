package com.payflow.wallet.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.payflow.wallet.application.provided.WalletTransactionModifier;
import com.payflow.wallet.application.required.WalletTransactionRepository;
import com.payflow.wallet.domain.Wallet;
import com.payflow.wallet.domain.WalletTransaction;
import com.payflow.wallet.domain.dto.WalletTransactionCreateRequest;

import lombok.RequiredArgsConstructor;

/**
 * @description    :
 */

@Service
@Transactional
@RequiredArgsConstructor
public class WalletTransactionModifyService implements WalletTransactionModifier {
	private final WalletTransactionRepository walletTransactionRepository;

	@Override
	public WalletTransaction write(Wallet wallet, WalletTransactionCreateRequest request) {
		WalletTransaction walletTransaction = WalletTransaction.create(wallet, request);

		walletTransactionRepository.save(walletTransaction);

		return walletTransaction;
	}
}
