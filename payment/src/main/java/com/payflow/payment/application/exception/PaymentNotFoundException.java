package com.payflow.payment.application.exception;

import org.springframework.http.HttpStatus;

import com.payflow.common.domain.PddPayException;

/**
 * @description    :
 */
public class PaymentNotFoundException extends PddPayException {
	private static final String MESSAGE = "해당하는 결제건이 존재하지 않습니다. paymentId : ";

	public PaymentNotFoundException(Long paymentId) {
		super(MESSAGE + paymentId);
	}

	@Override
	public HttpStatus getStatusCode() {
		return HttpStatus.NOT_FOUND;
	}
}
