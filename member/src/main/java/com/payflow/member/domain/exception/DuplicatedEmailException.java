package com.payflow.member.domain.exception;

/**
 * @description    :
 */
public class DuplicatedEmailException extends RuntimeException {
	private static final String MESSAGE = "이미 사용중인 이메일입니다: ";

	public DuplicatedEmailException(String email) {
		super(MESSAGE + email);
	}
}
