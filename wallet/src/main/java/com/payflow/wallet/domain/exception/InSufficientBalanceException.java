package com.payflow.wallet.domain.exception;

import org.springframework.http.HttpStatus;

import com.payflow.common.domain.PddPayException;

/**
 * @description    :
 */
public class InSufficientBalanceException extends PddPayException {
	private static final String MESSAGE = "잔액이 충분하지 않습니다. 잔액: ";

	public InSufficientBalanceException(Long balance) {
		super(MESSAGE + balance);
	}

	@Override
	public HttpStatus getStatusCode() {
		return HttpStatus.BAD_REQUEST;
	}
}
