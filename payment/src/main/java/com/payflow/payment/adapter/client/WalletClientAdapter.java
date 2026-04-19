package com.payflow.payment.adapter.client;

import org.springframework.stereotype.Component;

import com.payflow.payment.application.dto.WalletChangeRequest;
import com.payflow.payment.application.required.WalletClient;

/**
 * @description    :
 */
@Component
public class WalletClientAdapter implements WalletClient {

	@Override
	public void deduct(WalletChangeRequest request) {

	}

	@Override
	public void refund(WalletChangeRequest request) {

	}
}
