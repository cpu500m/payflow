package com.payflow.payment.adapter.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.payflow.payment.adapter.config.MemberClientConfig;

/**
 * @description    :
 */

@Component
@FeignClient(name = "member-service", configuration = MemberClientConfig.class)
public interface MemberFeignClient {
	@GetMapping("/api/members/{memberId}/validate")
	void validateActiveMember(@PathVariable Long memberId);
}
