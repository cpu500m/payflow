package com.payflow.settlement.adapter.in.messaging;

import com.payflow.settlement.application.required.SettlementTargetRepository;
import com.payflow.settlement.domain.SettlementTarget;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
public class PaymentEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(PaymentEventConsumer.class);

    private final SettlementTargetRepository settlementTargetRepository;

    public PaymentEventConsumer(SettlementTargetRepository settlementTargetRepository) {
        this.settlementTargetRepository = settlementTargetRepository;
    }

    @KafkaListener(topics = "payment.approved", groupId = "settlement-service")
    public void handlePaymentApproved(PaymentEvent event) {
        log.info("Received payment approved event: paymentId={}, merchantId={}, amount={}",
                event.paymentId(), event.merchantId(), event.amount());

        SettlementTarget target = new SettlementTarget(
                event.paymentId(),
                event.merchantId(),
                event.amount(),
                "APPROVED",
                event.eventAt() != null ? event.eventAt() : LocalDateTime.now()
        );

        settlementTargetRepository.save(target);
    }

    @KafkaListener(topics = "payment.cancelled", groupId = "settlement-service")
    public void handlePaymentCancelled(PaymentEvent event) {
        log.info("Received payment cancelled event: paymentId={}, merchantId={}, amount={}",
                event.paymentId(), event.merchantId(), event.amount());

        SettlementTarget target = new SettlementTarget(
                event.paymentId(),
                event.merchantId(),
                event.amount(),
                "CANCELLED",
                event.eventAt() != null ? event.eventAt() : LocalDateTime.now()
        );

        settlementTargetRepository.save(target);
    }

    public record PaymentEvent(
            Long paymentId,
            Long merchantId,
            BigDecimal amount,
            LocalDateTime eventAt
    ) {}
}
