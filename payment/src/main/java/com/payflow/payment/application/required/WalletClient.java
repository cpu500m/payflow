package com.payflow.payment.application.required;

import com.payflow.payment.application.dto.WalletChangeRequest;

/**
 * @description    :
 */
public interface WalletClient {
	void deduct(WalletChangeRequest request);

	void refund(WalletChangeRequest request);
}
