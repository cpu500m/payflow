package com.payflow.wallet.domain.exception;

import org.springframework.http.HttpStatus;

import com.payflow.common.domain.PddPayException;

/**
 * @description    :
 */
public class WalletAlreadyExistException extends PddPayException {
	private static final String MESSAGE = "해당 회원은 이미 지갑을 보유하고 있습니다. memberId: ";

	public WalletAlreadyExistException(Long memberId) {
		super(MESSAGE + memberId);
	}

	@Override
	public HttpStatus getStatusCode() {
		return HttpStatus.CONFLICT;
	}
}
