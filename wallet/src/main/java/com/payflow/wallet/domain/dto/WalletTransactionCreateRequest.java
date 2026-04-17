package com.payflow.wallet.domain.dto;

import org.hibernate.validator.constraints.Length;

import com.payflow.wallet.domain.enums.TransactionType;

import jakarta.validation.constraints.Min;

/**
 * @description    :
 */
public record WalletTransactionCreateRequest(
	TransactionType type,
	@Min(0) Long amount,
	@Length(max = 100) String description
) {
	public static WalletTransactionCreateRequest of(TransactionType type, WalletBalanceRequest request) {
		return new WalletTransactionCreateRequest(type, request.amount(), request.description());
	}
}
