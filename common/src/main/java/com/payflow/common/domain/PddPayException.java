package com.payflow.common.domain;

import org.springframework.http.HttpStatus;

/**
 * @description : 공통 비즈니스 exception
 */
public abstract class PddPayException extends RuntimeException {

	public PddPayException(String message) {
		super(message);
	}

	public abstract HttpStatus getStatusCode();
}
