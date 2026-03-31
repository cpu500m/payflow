package com.payflow.notification.adapter.in.messaging;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // === Exchanges ===

    @Bean
    public TopicExchange notificationExchange() {
        return new TopicExchange("notification.exchange");
    }

    @Bean
    public DirectExchange deadLetterExchange() {
        return new DirectExchange("notification.dlx");
    }

    // === Queues ===

    @Bean
    public Queue paymentNotificationQueue() {
        return QueueBuilder.durable("notification.payment.queue")
                .withArgument("x-dead-letter-exchange", "notification.dlx")
                .withArgument("x-dead-letter-routing-key", "notification.dlq")
                .build();
    }

    @Bean
    public Queue settlementNotificationQueue() {
        return QueueBuilder.durable("notification.settlement.queue")
                .withArgument("x-dead-letter-exchange", "notification.dlx")
                .withArgument("x-dead-letter-routing-key", "notification.dlq")
                .build();
    }

    @Bean
    public Queue deadLetterQueue() {
        return QueueBuilder.durable("notification.dlq")
                .withArgument("x-message-ttl", 60000)
                .withArgument("x-dead-letter-exchange", "notification.dlx")
                .withArgument("x-dead-letter-routing-key", "notification.failed")
                .build();
    }

    @Bean
    public Queue failedQueue() {
        return QueueBuilder.durable("notification.failed.queue")
                .build();
    }

    // === Bindings ===

    @Bean
    public Binding paymentQueueBinding(Queue paymentNotificationQueue, TopicExchange notificationExchange) {
        return BindingBuilder.bind(paymentNotificationQueue)
                .to(notificationExchange)
                .with("payment.*");
    }

    @Bean
    public Binding settlementQueueBinding(Queue settlementNotificationQueue, TopicExchange notificationExchange) {
        return BindingBuilder.bind(settlementNotificationQueue)
                .to(notificationExchange)
                .with("settlement.*");
    }

    @Bean
    public Binding deadLetterQueueBinding(Queue deadLetterQueue, DirectExchange deadLetterExchange) {
        return BindingBuilder.bind(deadLetterQueue)
                .to(deadLetterExchange)
                .with("notification.dlq");
    }

    @Bean
    public Binding failedQueueBinding(Queue failedQueue, DirectExchange deadLetterExchange) {
        return BindingBuilder.bind(failedQueue)
                .to(deadLetterExchange)
                .with("notification.failed");
    }

    // === Message Converter ===

    @Bean
    public MessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
