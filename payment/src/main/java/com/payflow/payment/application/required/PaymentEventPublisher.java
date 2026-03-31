package com.payflow.payment.application.required;

import com.payflow.payment.domain.Payment;

public interface PaymentEventPublisher {

    void publishApproved(Payment payment);

    void publishCancelled(Payment payment);

    void publishFailed(Payment payment);
}
