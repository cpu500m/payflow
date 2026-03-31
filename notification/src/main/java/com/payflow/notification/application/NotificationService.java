package com.payflow.notification.application;

import com.payflow.notification.application.provided.SendNotification;
import com.payflow.notification.application.required.NotificationSender;
import com.payflow.notification.domain.Notification;
import org.springframework.stereotype.Service;

@Service
public class NotificationService implements SendNotification {

    private final NotificationSender notificationSender;

    public NotificationService(NotificationSender notificationSender) {
        this.notificationSender = notificationSender;
    }

    @Override
    public void send(NotificationCommand command) {
        Notification notification = Notification.of(
                command.type(),
                command.recipientId(),
                command.title(),
                command.message()
        );
        notificationSender.send(notification);
    }
}
