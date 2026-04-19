package com.payflow.member.domain.exception;

import org.springframework.http.HttpStatus;

import com.payflow.common.domain.PddPayException;

/**
 * @description    :
 */
public class MemberNotFoundException extends PddPayException {
	private static final String MESSAGE = "해당하는 id의 회원이 존재하지 않습니다. ";

	public MemberNotFoundException(Long memberId) {
		super(MESSAGE + memberId);
	}

	@Override
	public HttpStatus getStatusCode() {
		return HttpStatus.NOT_FOUND;
	}
}
