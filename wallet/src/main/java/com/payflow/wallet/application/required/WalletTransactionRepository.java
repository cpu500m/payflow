package com.payflow.wallet.application.required;

import java.util.List;

import org.springframework.data.repository.Repository;

import com.payflow.wallet.domain.WalletTransaction;

/**
 * @description    :
 */
public interface WalletTransactionRepository extends Repository<WalletTransaction,Long> {
	WalletTransaction save(WalletTransaction walletTransaction);

	List<WalletTransaction> findByWalletIdOrderByCreatedAtDesc(Long walletId);
}
