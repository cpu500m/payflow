package com.payflow.wallet.domain.exception;

import org.springframework.http.HttpStatus;

import com.payflow.common.domain.PddPayException;

/**
 * @description    :
 */
public class WalletNotFoundException extends PddPayException {
	private static final String MESSAGE = "해당하는 회원의 지갑이 존재하지 않습니다. memberId : ";

	public WalletNotFoundException(Long memberId) {
		super(MESSAGE + memberId);
	}

	@Override
	public HttpStatus getStatusCode() {
		return HttpStatus.NOT_FOUND;
	}
}
