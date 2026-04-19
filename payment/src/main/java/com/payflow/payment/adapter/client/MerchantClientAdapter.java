package com.payflow.payment.adapter.client;

import org.springframework.stereotype.Component;

import com.payflow.payment.application.required.MerchantClient;

/**
 * @description    :
 */
@Component
public class MerchantClientAdapter implements MerchantClient {
	@Override
	public void validateActiveMerchant(Long merchantId) {

	}
}
