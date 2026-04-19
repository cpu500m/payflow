package com.payflow.payment.application.provided;

import com.payflow.payment.domain.Payment;

/**
 * @description    :
 */
public interface PaymentFinder {
	Payment find(Long paymentId);
}
