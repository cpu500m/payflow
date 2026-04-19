package com.payflow.wallet.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.payflow.wallet.application.provided.WalletFinder;
import com.payflow.wallet.application.required.WalletRepository;
import com.payflow.wallet.domain.Wallet;
import com.payflow.wallet.domain.exception.WalletNotFoundException;

import lombok.RequiredArgsConstructor;

/**
 * @description    :
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class WalletQueryService implements WalletFinder {

	private final WalletRepository walletRepository;

	@Override
	public Wallet findByMemberId(Long memberId) {
		return walletRepository.findByMemberId(memberId)
			.orElseThrow(() -> WalletNotFoundException.byMember(memberId));
	}

	@Override
	public Long getBalance(Long memberId) {
		Wallet wallet = findByMemberId(memberId);

		return wallet.getBalance();
	}
}
