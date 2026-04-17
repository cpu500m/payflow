package com.payflow.wallet.application.provided;

import com.payflow.wallet.domain.dto.WalletBalanceRequest;
import com.payflow.wallet.domain.Wallet;
import com.payflow.wallet.domain.dto.WalletCreateRequest;

import jakarta.validation.Valid;

/**
 * @description    :
 */
public interface WalletModifier {
	Wallet create(@Valid WalletCreateRequest request);

	Wallet charge(Long memberId, WalletBalanceRequest request);

	Wallet deduct(Long memberId, WalletBalanceRequest request);

	Wallet refund(Long memberId, WalletBalanceRequest request);
}
