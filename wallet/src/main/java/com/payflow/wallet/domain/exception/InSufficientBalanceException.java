package com.payflow.wallet.domain.exception;

/**
 * @description    :
 */
public class InSufficientBalanceException extends RuntimeException {
	private static final String MESSAGE = "잔액이 충분하지 않습니다. 잔액: ";

	public InSufficientBalanceException(Long balance) {
		super(MESSAGE + balance);
	}
}
