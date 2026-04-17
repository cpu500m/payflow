package com.payflow.wallet.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.payflow.wallet.domain.dto.WalletBalanceRequest;
import com.payflow.wallet.application.provided.WalletFinder;
import com.payflow.wallet.application.provided.WalletModifier;
import com.payflow.wallet.application.provided.WalletTransactionModifier;
import com.payflow.wallet.application.required.WalletRepository;
import com.payflow.wallet.domain.Wallet;
import com.payflow.wallet.domain.dto.WalletCreateRequest;
import com.payflow.wallet.domain.dto.WalletTransactionCreateRequest;
import com.payflow.wallet.domain.enums.TransactionType;
import com.payflow.wallet.domain.exception.WalletAlreadyExistException;

import lombok.RequiredArgsConstructor;

/**
 * @description    :
 */

@Service
@Transactional
@RequiredArgsConstructor
@Validated
public class WalletModifyService implements WalletModifier {

	private final WalletFinder walletFinder;
	private final WalletTransactionModifier walletTransactionModifier;

	private final WalletRepository walletRepository;

	@Override
	public Wallet create(WalletCreateRequest request) {
		checkDuplicateWallet(request);

		Wallet wallet = Wallet.create(request);

		walletRepository.save(wallet);

		return wallet;
	}

	@Override
	public Wallet charge(Long memberId, WalletBalanceRequest request) {
		Wallet wallet = walletFinder.findByMemberId(memberId);

		wallet.charge(request.amount());

		WalletTransactionCreateRequest transactionCreateRequest =
			WalletTransactionCreateRequest.of(TransactionType.CHARGE, request);
		walletTransactionModifier.write(wallet, transactionCreateRequest);

		return walletRepository.save(wallet);
	}

	@Override
	public Wallet deduct(Long memberId, WalletBalanceRequest request) {
		Wallet wallet = walletFinder.findByMemberId(memberId);

		wallet.deduct(request.amount());

		WalletTransactionCreateRequest transactionCreateRequest =
			WalletTransactionCreateRequest.of(TransactionType.DEDUCT, request);
		walletTransactionModifier.write(wallet, transactionCreateRequest);

		return walletRepository.save(wallet);
	}

	@Override
	public Wallet refund(Long memberId, WalletBalanceRequest request) {
		Wallet wallet = walletFinder.findByMemberId(memberId);

		wallet.refund(request.amount());

		WalletTransactionCreateRequest transactionCreateRequest =
			WalletTransactionCreateRequest.of(TransactionType.REFUND, request);
		walletTransactionModifier.write(wallet, transactionCreateRequest);

		return walletRepository.save(wallet);
	}

	private void checkDuplicateWallet(WalletCreateRequest request) {
		if (walletRepository.findByMemberId(request.memberId()).isPresent()) {
			throw new WalletAlreadyExistException(request.memberId());
		}
	}
}
