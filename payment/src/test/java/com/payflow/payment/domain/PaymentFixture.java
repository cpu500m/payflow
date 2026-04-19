package com.payflow.payment.domain;

/**
 * @description    :
 */
public class PaymentFixture {

	public static PaymentRequest createPaymentRequest(Long amount) {
		return new PaymentRequest(amount, 1L, 1L);
	}

	public static PaymentRequest createPaymentRequest() {
		return createPaymentRequest(10000L);
	}

	public static Payment createRequestedPayment() {
		return Payment.request(createPaymentRequest());
	}

	public static Payment createApprovedPayment() {
		Payment payment = createRequestedPayment();
		payment.approve();
		return payment;
	}
}