package com.payflow.payment.adapter.kafka;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import com.fasterxml.jackson.databind.JsonSerializer;
import com.payflow.payment.application.dto.PaymentEvent;

import tools.jackson.databind.ser.jdk.StringSerializer;

/**
 * @description  : producer 설정
 * 결제 이벤트 유실 시 문제가 크다고 생각하여 전체 브로커로부터 적재 응답을 받게 설정 ( ACKS_CONFIG : all )
 */
@Configuration
public class KafkaProducerConfig {

	@Bean
	public ProducerFactory<String, PaymentEvent> producerFactory(
		@Value("${spring.kafka.bootstrap-servers}") String bootstrapServers) {

		Map<String, Object> props = new HashMap<>();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
		props.put(ProducerConfig.ACKS_CONFIG, "all");

		return new DefaultKafkaProducerFactory<>(props);
	}

	@Bean
	public KafkaTemplate<String, PaymentEvent> kafkaTemplate(
		ProducerFactory<String, PaymentEvent> producerFactory) {
		return new KafkaTemplate<>(producerFactory);
	}
}
