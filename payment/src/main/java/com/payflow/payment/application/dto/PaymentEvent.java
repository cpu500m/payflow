package com.payflow.payment.application.dto;

import java.time.LocalDateTime;

import com.payflow.payment.domain.Payment;

/**
 * @description    :
 */
public record PaymentEvent(
	Long paymentId,
	Long merchantId,
	Long amount,
	String status,
	LocalDateTime occurredAt
) {
	public static PaymentEvent approved(Payment payment) {
		return new PaymentEvent(
			payment.getId(),
			payment.getMerchantId(),
			payment.getAmount(),
			"APPROVED",
			LocalDateTime.now()
		);
	}

	public static PaymentEvent canceled(Payment payment) {
		return new PaymentEvent(
			payment.getId(),
			payment.getMerchantId(),
			payment.getAmount(),
			"CANCELED",
			LocalDateTime.now()
		);
	}
}