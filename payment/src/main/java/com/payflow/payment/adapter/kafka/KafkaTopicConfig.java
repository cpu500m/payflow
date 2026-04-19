package com.payflow.payment.adapter.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

/**
 * @description    :
 */
@Configuration
public class KafkaTopicConfig {

	@Bean
	public NewTopic paymentTopic(
		@Value("${app.environment}") String environment,
		@Value("${app.kafka.partitions}") int partitions,
		@Value("${app.kafka.replicas}") int replicas) {
		return TopicBuilder
			.name(environment + ".payment.json")
			.partitions(partitions)
			.replicas(replicas)
			.build();
	}
}
