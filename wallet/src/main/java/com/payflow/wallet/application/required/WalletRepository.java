package com.payflow.wallet.application.required;

import java.util.Optional;

import org.springframework.data.repository.Repository;

import com.payflow.wallet.domain.Wallet;

/**
 * @description    :
 */
public interface WalletRepository extends Repository<Wallet, Long> {
	Wallet save(Wallet wallet);

	Optional<Wallet> findById(Long walletId);

	Optional<Wallet> findByMemberId(Long memberId);
}
