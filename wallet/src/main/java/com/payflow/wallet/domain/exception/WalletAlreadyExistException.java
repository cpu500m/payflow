package com.payflow.wallet.domain.exception;

/**
 * @description    :
 */
public class WalletAlreadyExistException extends RuntimeException {
	private static final String MESSAGE = "해당 회원은 이미 지갑을 보유하고 있습니다. memberId: ";

	public WalletAlreadyExistException(Long memberId) {
		super(MESSAGE + memberId);
	}
}
