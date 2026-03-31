package com.payflow.notification.domain;

import java.time.LocalDateTime;

public class Notification {

    private final NotificationType type;
    private final Long recipientId;
    private final String title;
    private final String message;
    private final LocalDateTime createdAt;

    private Notification(NotificationType type, Long recipientId, String title, String message, LocalDateTime createdAt) {
        this.type = type;
        this.recipientId = recipientId;
        this.title = title;
        this.message = message;
        this.createdAt = createdAt;
    }

    public static Notification of(NotificationType type, Long recipientId, String title, String message) {
        return new Notification(type, recipientId, title, message, LocalDateTime.now());
    }

    public NotificationType getType() {
        return type;
    }

    public Long getRecipientId() {
        return recipientId;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "type=" + type +
                ", recipientId=" + recipientId +
                ", title='" + title + '\'' +
                ", message='" + message + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
