package com.payflow.wallet.adapter.webapi.dto;

/**
 * @description    :
 */
public record WalletBalanceResponse(
	Long memberId,
	Long balance
) {
	public static WalletBalanceResponse of(Long memberId, Long balance) {
		return new WalletBalanceResponse(memberId, balance);
	}
}