package com.payflow.notification.application.provided;

import com.payflow.notification.domain.NotificationType;

public interface SendNotification {

    void send(NotificationCommand command);

    record NotificationCommand(
            NotificationType type,
            Long recipientId,
            String title,
            String message
    ) {
    }
}
