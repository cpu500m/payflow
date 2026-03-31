package com.payflow.payment.application;

import com.payflow.payment.application.provided.CancelPayment;
import com.payflow.payment.application.provided.RequestPayment;
import com.payflow.payment.application.required.PaymentEventPublisher;
import com.payflow.payment.application.required.PaymentRepository;
import com.payflow.payment.application.required.WalletClient;
import com.payflow.payment.application.required.WalletClient.DeductResult;
import com.payflow.payment.domain.Payment;
import com.payflow.payment.domain.PaymentMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PaymentService implements RequestPayment, CancelPayment {

    private final PaymentRepository paymentRepository;
    private final PaymentEventPublisher paymentEventPublisher;
    private final WalletClient walletClient;

    public PaymentService(PaymentRepository paymentRepository,
                          PaymentEventPublisher paymentEventPublisher,
                          WalletClient walletClient) {
        this.paymentRepository = paymentRepository;
        this.paymentEventPublisher = paymentEventPublisher;
        this.walletClient = walletClient;
    }

    @Override
    public PaymentResponse request(PaymentRequest request) {
        Payment payment = Payment.create(
                request.memberId(),
                request.merchantId(),
                request.amount(),
                request.method()
        );
        payment = paymentRepository.save(payment);

        if (request.method() == PaymentMethod.WALLET) {
            DeductResult result = walletClient.deduct(request.memberId(), request.amount());
            if (result == DeductResult.SUCCESS) {
                payment.approve();
                payment = paymentRepository.save(payment);
                paymentEventPublisher.publishApproved(payment);
            } else {
                payment.fail();
                payment = paymentRepository.save(payment);
                paymentEventPublisher.publishFailed(payment);
            }
        } else {
            // CARD payments: approve immediately (PG integration placeholder)
            payment.approve();
            payment = paymentRepository.save(payment);
            paymentEventPublisher.publishApproved(payment);
        }

        return new PaymentResponse(
                payment.getId(),
                payment.getStatus(),
                payment.getApprovedAt()
        );
    }

    @Override
    public PaymentResponse cancel(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Payment not found: " + paymentId));

        payment.cancel();

        if (payment.getMethod() == PaymentMethod.WALLET) {
            walletClient.refund(payment.getMemberId(), payment.getAmount());
        }

        payment = paymentRepository.save(payment);
        paymentEventPublisher.publishCancelled(payment);

        return new PaymentResponse(
                payment.getId(),
                payment.getStatus(),
                payment.getApprovedAt()
        );
    }
}
