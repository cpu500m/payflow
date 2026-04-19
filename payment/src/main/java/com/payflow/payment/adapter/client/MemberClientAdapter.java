package com.payflow.payment.adapter.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import lombok.RequiredArgsConstructor;

/**
 * @description    :
 */

@Component
@RequiredArgsConstructor
public class MemberClientAdapter {
	private final RestClient memberRestClient;
/*
	@Override
	public void validateActiveMember(Long memberId) {
		memberRestClient.get()
			.uri("/api/{memberId}/")
	}
	*/
}
