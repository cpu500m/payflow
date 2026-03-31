package com.payflow.payment.application.provided;

import com.payflow.payment.domain.PaymentMethod;
import com.payflow.payment.domain.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface RequestPayment {

    PaymentResponse request(PaymentRequest request);

    record PaymentRequest(
            Long memberId,
            Long merchantId,
            BigDecimal amount,
            PaymentMethod method
    ) {}

    record PaymentResponse(
            Long paymentId,
            PaymentStatus status,
            LocalDateTime approvedAt
    ) {}
}
