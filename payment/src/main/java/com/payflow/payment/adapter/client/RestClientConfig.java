package com.payflow.payment.adapter.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

/**
 * @description    :
 */
@Configuration
public class RestClientConfig {
	@Bean
	public RestClient walletRestClient(@Value("${service.wallet.url}") String walletDomain) {
		return RestClient.builder()
			.baseUrl(walletDomain)
			.build();
	}
	@Bean
	public RestClient memberRestClient(@Value("${service.member.url}") String memberDomain) {
		return RestClient.builder()
			.baseUrl(memberDomain)
			.build();
	}

	@Bean
	public RestClient merchantRestClient(@Value("${service.member.url}") String merchantDomain) {
		return RestClient.builder()
			.baseUrl(merchantDomain)
			.build();
	}

}
