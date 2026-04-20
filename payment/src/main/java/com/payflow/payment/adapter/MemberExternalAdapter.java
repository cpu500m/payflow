package com.payflow.payment.adapter;

import org.springframework.stereotype.Component;

import com.payflow.payment.adapter.client.MemberFeignClient;
import com.payflow.payment.application.required.MemberClient;

import lombok.RequiredArgsConstructor;

/**
 * @description    :
 */
@Component
@RequiredArgsConstructor
public class MemberExternalAdapter implements MemberClient {

	private final MemberFeignClient memberFeignClient;

	@Override
	public void validateActiveMember(Long memberId) {
		memberFeignClient.validateActiveMember(memberId);
	}
}
