package com.payflow.payment.application.dto;

/**
 * @description    :
 */
public record WalletChangeRequest(
	Long memberId,
	Long amount
) {
	public static WalletChangeRequest of(Long merchantId , Long amount) {
		return new WalletChangeRequest(merchantId ,amount);
	}
}
