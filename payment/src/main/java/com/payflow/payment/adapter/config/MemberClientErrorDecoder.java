package com.payflow.payment.adapter.config;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

import com.payflow.common.domain.PddPayException;

import feign.Response;
import feign.codec.ErrorDecoder;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.json.JsonMapper;

/**
 * @description    :
 */
public class MemberClientErrorDecoder implements ErrorDecoder {

	@Override
	public Exception decode(String methodKey, Response response) {
		return null;
	}
/*
	// JsonMapper 빌더를 사용하여 명시적으로 생성
	private final JsonMapper jsonMapper = JsonMapper.builder()
		.addModule(new JavaTimeModule()) // LocalDateTime 처리를 위한 모듈 추가
		.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false) // 모르는 필드는 무시
		.build();

	@Override
	public Exception decode(String methodKey, Response response) {
		try (InputStream bodyIs = response.body().asInputStream()) {
			// ProblemDetail 구조로 파싱
			ProblemDetail problemDetail = jsonMapper.readValue(bodyIs, ProblemDetail.class);

			String errorMessage = problemDetail.getDetail();
			HttpStatus status = HttpStatus.valueOf(response.status());

			// 비즈니스 예외로 변환
			return new PddPayException(errorMessage, status) {
				@Override
				public HttpStatus getStatusCode() {
					return status;
				}
			};

		} catch (IOException e) {
			return new Exception("멤버 서비스 응답 파싱 실패: " + e.getMessage());
		}
	}
	*/
}
