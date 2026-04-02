package com.payflow.member.adapter.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

/**
 * @description    : RestClient 설정
 */
@Configuration
public class RestClientConfig {

	@Bean
	public RestClient walletRestClient(@Value("${service.wallet.url}") String walletDomain) {

		return RestClient.builder()
			.baseUrl(walletDomain)
			.build();
	}
}
