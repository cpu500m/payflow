package com.payflow.merchant.domain.exception;

/**
 * @description    :
 */
public class DuplicateBusinessNameException extends RuntimeException {
	private static final String MESSAGE = "이미 등록된 사업자명입니다 : ";

	public DuplicateBusinessNameException(String businessName) {
		super(MESSAGE + businessName);
	}
}
