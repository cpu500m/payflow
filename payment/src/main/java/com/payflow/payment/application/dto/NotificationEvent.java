package com.payflow.payment.application.dto;

import java.time.LocalDateTime;

import com.payflow.payment.domain.Payment;

/**
 * @description    :
 */
public record NotificationEvent(
	Long paymentId,
	Long memberId,
	Long merchantId,
	Long amount,
	String status,
	LocalDateTime occurredAt
) {
	public static NotificationEvent approved(Payment payment) {
		return new NotificationEvent(
			payment.getId(),
			payment.getMemberId(),
			payment.getMerchantId(),
			payment.getAmount(),
			"APPROVED",
			LocalDateTime.now()
		);
	}

	public static NotificationEvent canceled(Payment payment) {
		return new NotificationEvent(
			payment.getId(),
			payment.getMemberId(),
			payment.getMerchantId(),
			payment.getAmount(),
			"CANCELED",
			LocalDateTime.now()
		);
	}
}