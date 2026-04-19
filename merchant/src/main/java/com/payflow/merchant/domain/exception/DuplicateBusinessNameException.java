package com.payflow.merchant.domain.exception;

import org.springframework.http.HttpStatus;

import com.payflow.common.domain.PddPayException;

/**
 * @description    :
 */
public class DuplicateBusinessNameException extends PddPayException {
	private static final String MESSAGE = "이미 등록된 사업자명입니다 : ";

	public DuplicateBusinessNameException(String businessName) {
		super(MESSAGE + businessName);
	}

	@Override
	public HttpStatus getStatusCode() {
		return HttpStatus.CONFLICT;
	}
}
