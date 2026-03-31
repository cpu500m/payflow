package com.payflow.payment.adapter.in.web;

import com.payflow.payment.application.provided.CancelPayment;
import com.payflow.payment.application.provided.RequestPayment;
import com.payflow.payment.application.provided.RequestPayment.PaymentRequest;
import com.payflow.payment.application.provided.RequestPayment.PaymentResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {

    private final RequestPayment requestPayment;
    private final CancelPayment cancelPayment;

    public PaymentController(RequestPayment requestPayment,
                             CancelPayment cancelPayment) {
        this.requestPayment = requestPayment;
        this.cancelPayment = cancelPayment;
    }

    @PostMapping
    public ResponseEntity<PaymentResponse> requestPayment(@RequestBody PaymentRequest request) {
        PaymentResponse response = requestPayment.request(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<PaymentResponse> cancelPayment(@PathVariable Long id) {
        PaymentResponse response = cancelPayment.cancel(id);
        return ResponseEntity.ok(response);
    }
}
