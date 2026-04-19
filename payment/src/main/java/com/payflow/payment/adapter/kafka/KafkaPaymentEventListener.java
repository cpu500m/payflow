package com.payflow.payment.adapter.kafka;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.payflow.payment.application.dto.PaymentEvent;

import lombok.extern.slf4j.Slf4j;

/**
 * @description    :
 */

@Component
@Slf4j
public class KafkaPaymentEventListener {
	private final KafkaTemplate<String, PaymentEvent> kafkaTemplate;
	private String topic;

	public KafkaPaymentEventListener(
		KafkaTemplate<String, PaymentEvent> kafkaTemplate,
		@Value("${app.environment}") String environment) {
		this.kafkaTemplate = kafkaTemplate;
		this.topic = environment + ".payment.json";
	}

	@Async
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void handle(PaymentEvent event) {
		try {
			kafkaTemplate.send(topic, String.valueOf(event.merchantId()), event);
			log.info("[Kafka] 결제 이벤트 발행 완료 - paymentId: {}, status: {}", event.paymentId(), event.status());
		} catch (Exception e) {
			log.error("[Kafka] 결제 이벤트 발행 실패 - paymentId: {}, status: {}", event.paymentId(), event.status(), e);
		}
	}
}
