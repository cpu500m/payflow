package com.payflow.notification.application.required;

import com.payflow.notification.domain.Notification;

public interface NotificationSender {

    void send(Notification notification);
}
