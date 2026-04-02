package com.payflow.member.application.required;

/**
 * @description    :
 */
public interface WalletClient {
	void createWallet(Long memberId);

	long getWalletBalance(Long memberId);
}
