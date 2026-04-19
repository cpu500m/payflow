package com.payflow.wallet.domain.dto;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.Min;

/**
 * @description :
 */
public record WalletBalanceRequest(
	@Min(1) Long amount,
	@Length(max = 100) String description
) {
}
