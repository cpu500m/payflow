package com.payflow.common.adapter;

import java.time.LocalDateTime;

import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.payflow.common.domain.PddPayException;

/**
 * @description : exception 처리 공통 controller advice
 */
@ControllerAdvice
public class CommonApiControllerAdvice extends ResponseEntityExceptionHandler {

	@ExceptionHandler(Exception.class)
	public ProblemDetail handleException(Exception exception) {
		return getProblemDetail(HttpStatus.INTERNAL_SERVER_ERROR, exception);
	}

	@ExceptionHandler(PddPayException.class)
	public ProblemDetail handleBusinessException(PddPayException exception) {
		return getProblemDetail(exception.getStatusCode(), exception);
	}

	private static @NonNull ProblemDetail getProblemDetail(HttpStatus status, Exception exception) {
		ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, exception.getMessage());

		problemDetail.setProperty("timestamp", LocalDateTime.now());
		problemDetail.setProperty("exception", exception.getClass().getSimpleName());
		return problemDetail;
	}
}
