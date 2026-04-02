package com.payflow.member.domain.exception;

/**
 * @description    :
 */
public class WalletBalanceNotZeroException extends RuntimeException {
	private static final String MESSAGE = "지갑 잔액이 존재합니다. :";

	public WalletBalanceNotZeroException(long balance) {
		super(MESSAGE + balance);
	}
}
