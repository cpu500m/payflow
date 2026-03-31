package com.payflow.payment.application.required;

import com.payflow.payment.domain.Payment;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository {

    Payment save(Payment payment);

    Optional<Payment> findById(Long id);

    List<Payment> findByMemberIdOrderByCreatedAtDesc(Long memberId);
}
