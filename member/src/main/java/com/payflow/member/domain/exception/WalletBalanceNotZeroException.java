package com.payflow.member.domain.exception;

import org.springframework.http.HttpStatus;

import com.payflow.common.domain.PddPayException;

/**
 * @description    :
 */
public class WalletBalanceNotZeroException extends PddPayException {
	private static final String MESSAGE = "지갑 잔액이 존재합니다. :";

	public WalletBalanceNotZeroException(long balance) {
		super(MESSAGE + balance);
	}

	@Override
	public HttpStatus getStatusCode() {
		return HttpStatus.BAD_REQUEST;
	}
}
