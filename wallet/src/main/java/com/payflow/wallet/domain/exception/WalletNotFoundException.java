package com.payflow.wallet.domain.exception;

/**
 * @description    :
 */
public class WalletNotFoundException extends RuntimeException {
	private WalletNotFoundException(String message) {
		super(message);
	}

	public static WalletNotFoundException byWallet(Long walletId) {
		return new WalletNotFoundException("해당하는 id의 지갑이 존재하지 않습니다. : " + walletId);
	}

	public static WalletNotFoundException byMember(Long memberId) {
		return new WalletNotFoundException("해당하는 회원의 지갑이 존재하지 않습니다. memberId : " + memberId);
	}
}
