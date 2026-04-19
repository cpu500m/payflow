package com.payflow.payment.adapter.exception;

import org.springframework.http.HttpStatus;

import com.payflow.common.domain.PddPayException;

/**
 * @description    : 락 획득 실패 exception
 */
public class PaymentLockException extends PddPayException {
	private static final String MESSAGE = "이미 결제 진행중인 회원입니다. memberId: ";

	public PaymentLockException(Long memberId) {
		super(MESSAGE + memberId);
	}

	@Override
	public HttpStatus getStatusCode() {
			return HttpStatus.CONFLICT;
	}
}
