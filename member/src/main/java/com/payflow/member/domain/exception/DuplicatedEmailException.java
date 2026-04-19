package com.payflow.member.domain.exception;

import org.springframework.http.HttpStatus;

import com.payflow.common.domain.PddPayException;

/**
 * @description    :
 */
public class DuplicatedEmailException extends PddPayException {
	private static final String MESSAGE = "이미 사용중인 이메일입니다: ";

	public DuplicatedEmailException(String email) {
		super(MESSAGE + email);
	}

	@Override
	public HttpStatus getStatusCode() {
		return HttpStatus.CONFLICT;
	}
}
