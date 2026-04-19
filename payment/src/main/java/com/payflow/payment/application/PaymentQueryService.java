package com.payflow.payment.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.payflow.payment.application.exception.PaymentNotFoundException;
import com.payflow.payment.application.provided.PaymentFinder;
import com.payflow.payment.application.required.PaymentRepository;
import com.payflow.payment.domain.Payment;

import lombok.RequiredArgsConstructor;

/**
 * @description    :
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentQueryService implements PaymentFinder {

	private final PaymentRepository paymentRepository;

	@Override
	public Payment find(Long paymentId) {
		return paymentRepository.findById(paymentId)
			.orElseThrow(() -> new PaymentNotFoundException(paymentId));
	}
}
