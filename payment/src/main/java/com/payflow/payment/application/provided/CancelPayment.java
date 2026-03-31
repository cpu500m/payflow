package com.payflow.payment.application.provided;

import com.payflow.payment.application.provided.RequestPayment.PaymentResponse;

public interface CancelPayment {

    PaymentResponse cancel(Long paymentId);
}
