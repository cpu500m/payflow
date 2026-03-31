package com.payflow.notification.adapter.in.messaging;

import com.payflow.notification.application.provided.SendNotification;
import com.payflow.notification.application.provided.SendNotification.NotificationCommand;
import com.payflow.notification.domain.NotificationType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class NotificationEventListener {

    private static final Logger log = LoggerFactory.getLogger(NotificationEventListener.class);

    private final SendNotification sendNotification;

    public NotificationEventListener(SendNotification sendNotification) {
        this.sendNotification = sendNotification;
    }

    @RabbitListener(queues = "notification.payment.queue")
    public void handlePaymentNotification(Map<String, Object> message) {
        log.info("Received payment notification message: {}", message);
        try {
            NotificationCommand command = toCommand(message);
            sendNotification.send(command);
        } catch (Exception e) {
            log.error("Failed to process payment notification: {}", message, e);
            throw e;
        }
    }

    @RabbitListener(queues = "notification.settlement.queue")
    public void handleSettlementNotification(Map<String, Object> message) {
        log.info("Received settlement notification message: {}", message);
        try {
            NotificationCommand command = toCommand(message);
            sendNotification.send(command);
        } catch (Exception e) {
            log.error("Failed to process settlement notification: {}", message, e);
            throw e;
        }
    }

    private NotificationCommand toCommand(Map<String, Object> message) {
        NotificationType type = NotificationType.valueOf((String) message.get("type"));
        Long recipientId = ((Number) message.get("recipientId")).longValue();
        String title = (String) message.get("title");
        String messageBody = (String) message.get("message");
        return new NotificationCommand(type, recipientId, title, messageBody);
    }
}
