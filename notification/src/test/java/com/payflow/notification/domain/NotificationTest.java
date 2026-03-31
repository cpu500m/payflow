package com.payflow.notification.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class NotificationTest {

    @Test
    @DisplayName("Notification.of creates a notification with all fields set")
    void createNotification() {
        // given
        NotificationType type = NotificationType.PAYMENT_APPROVED;
        Long recipientId = 1001L;
        String title = "Payment Approved";
        String message = "Your payment of $100.00 has been approved.";

        // when
        LocalDateTime beforeCreation = LocalDateTime.now();
        Notification notification = Notification.of(type, recipientId, title, message);
        LocalDateTime afterCreation = LocalDateTime.now();

        // then
        assertThat(notification.getType()).isEqualTo(NotificationType.PAYMENT_APPROVED);
        assertThat(notification.getRecipientId()).isEqualTo(1001L);
        assertThat(notification.getTitle()).isEqualTo("Payment Approved");
        assertThat(notification.getMessage()).isEqualTo("Your payment of $100.00 has been approved.");
        assertThat(notification.getCreatedAt()).isBetween(beforeCreation, afterCreation);
    }

    @Test
    @DisplayName("Notification.of supports all notification types")
    void createNotificationWithDifferentTypes() {
        for (NotificationType type : NotificationType.values()) {
            Notification notification = Notification.of(type, 1L, "Title", "Message");
            assertThat(notification.getType()).isEqualTo(type);
        }
    }

    @Test
    @DisplayName("toString contains all notification fields")
    void toStringContainsAllFields() {
        Notification notification = Notification.of(
                NotificationType.SETTLEMENT_COMPLETED, 42L, "Settlement Done", "Your settlement is complete."
        );

        String result = notification.toString();

        assertThat(result).contains("SETTLEMENT_COMPLETED");
        assertThat(result).contains("42");
        assertThat(result).contains("Settlement Done");
        assertThat(result).contains("Your settlement is complete.");
    }
}
