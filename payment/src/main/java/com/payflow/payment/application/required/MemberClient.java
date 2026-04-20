package com.payflow.payment.application.required;

import org.springframework.cloud.openfeign.FeignClient;

/**
 * @description    :
 */

@FeignClient
public interface MemberClient {
	void validateActiveMember(Long memberId);
}
