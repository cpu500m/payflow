package com.payflow.payment.adapter.config;

import org.springframework.context.annotation.Bean;

import feign.Logger;
import feign.RequestInterceptor;
import feign.codec.ErrorDecoder;

/**
 * @description    :
 */
public class MemberClientConfig {
	@Bean
	public ErrorDecoder errorDecoder() {
		// 멤버 서비스의 응답 규격(ProblemDetail)에 맞춘 커스텀 에러 디코더
		return new MemberClientErrorDecoder();
	}

	@Bean
	Logger.Level feignLoggerLevel() {
		// 개발 환경에서는 FULL, 운영에서는 BASIC 등으로 설정 가능
		return Logger.Level.FULL;
	}

	@Bean
	public RequestInterceptor requestInterceptor() {
		// 모든 멤버 서비스 호출 시 공통으로 넣어야 할 헤더가 있다면 정의
		return requestTemplate -> {
			requestTemplate.header("Content-Type", "application/json");
			// 예: 내부 서비스 인증 토큰 등
			requestTemplate.header("X-Internal-Service-Name", "pdd-payment");
		};
	}
}
