package com.payflow.wallet.domain;

import java.time.LocalDateTime;

import com.payflow.common.domain.AbstractEntity;
import com.payflow.wallet.domain.dto.WalletTransactionCreateRequest;
import com.payflow.wallet.domain.enums.TransactionType;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @description  : 거래 내역 entity
 */
@Entity
@Table(name = "Transaction_Ledger")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WalletTransaction extends AbstractEntity {
	private TransactionType type;

	private Long amount;

	private String description;

	private Long balanceAfter;

	private LocalDateTime createdAt;

	private Wallet wallet;

	public static WalletTransaction create(Wallet wallet, WalletTransactionCreateRequest createRequest) {
		WalletTransaction transaction = new WalletTransaction();
		transaction.type = createRequest.type();
		transaction.amount = createRequest.amount();
		transaction.description = createRequest.description();
		transaction.balanceAfter = wallet.getBalance();
		transaction.wallet = wallet;

		transaction.createdAt = LocalDateTime.now();

		return transaction;
	}
}
