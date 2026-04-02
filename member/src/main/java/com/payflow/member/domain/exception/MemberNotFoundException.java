package com.payflow.member.domain.exception;

/**
 * @description    :
 */
public class MemberNotFoundException extends RuntimeException {
	private static final String MESSAGE = "해당하는 id의 회원이 존재하지 않습니다. ";

	public MemberNotFoundException(Long memberId) {
		super(MESSAGE + memberId);
	}
}
