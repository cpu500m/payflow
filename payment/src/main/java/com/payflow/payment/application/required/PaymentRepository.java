package com.payflow.payment.application.required;

import java.util.Optional;

import org.springframework.data.repository.Repository;

import com.payflow.payment.domain.Payment;

/**
 * @description    :
 */
public interface PaymentRepository extends Repository<Payment, Long> {
	Payment save(Payment payment);

	Optional<Payment> findById(Long paymentId);
}
