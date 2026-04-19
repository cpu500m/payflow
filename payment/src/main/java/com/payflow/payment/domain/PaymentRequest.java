package com.payflow.payment.domain;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * @description    :
 */
public record PaymentRequest(
	@Min(1) Long amount,
	@NotNull Long memberId,
	@NotNull Long merchantId
) {
}
