package com.payflow.payment.application.provided;

import com.payflow.payment.domain.Payment;
import com.payflow.payment.domain.PaymentRequest;

/**
 * @description    :
 */
public interface PaymentModifier {
	Payment request(PaymentRequest request);
	Payment cancel(Long paymentId);
}