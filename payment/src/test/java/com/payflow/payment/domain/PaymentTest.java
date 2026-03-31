package com.payflow.payment.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PaymentTest {

    @Test
    @DisplayName("Payment.create() sets initial status to REQUESTED")
    void create_shouldSetStatusToRequested() {
        Payment payment = Payment.create(1L, 100L, BigDecimal.valueOf(50000), PaymentMethod.WALLET);

        assertThat(payment.getMemberId()).isEqualTo(1L);
        assertThat(payment.getMerchantId()).isEqualTo(100L);
        assertThat(payment.getAmount()).isEqualByComparingTo(BigDecimal.valueOf(50000));
        assertThat(payment.getMethod()).isEqualTo(PaymentMethod.WALLET);
        assertThat(payment.getStatus()).isEqualTo(PaymentStatus.REQUESTED);
        assertThat(payment.getCreatedAt()).isNotNull();
        assertThat(payment.getApprovedAt()).isNull();
    }

    @Test
    @DisplayName("approve() transitions REQUESTED to APPROVED and sets approvedAt")
    void approve_shouldTransitionToApproved() {
        Payment payment = Payment.create(1L, 100L, BigDecimal.valueOf(10000), PaymentMethod.CARD);

        payment.approve();

        assertThat(payment.getStatus()).isEqualTo(PaymentStatus.APPROVED);
        assertThat(payment.getApprovedAt()).isNotNull();
    }

    @Test
    @DisplayName("approve() throws when payment is not in REQUESTED status")
    void approve_shouldThrow_whenNotRequested() {
        Payment payment = Payment.create(1L, 100L, BigDecimal.valueOf(10000), PaymentMethod.CARD);
        payment.approve();

        assertThatThrownBy(payment::approve)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Cannot approve payment in status: APPROVED");
    }

    @Test
    @DisplayName("cancel() transitions APPROVED to CANCELLED")
    void cancel_shouldTransitionToCancelled() {
        Payment payment = Payment.create(1L, 100L, BigDecimal.valueOf(10000), PaymentMethod.WALLET);
        payment.approve();

        payment.cancel();

        assertThat(payment.getStatus()).isEqualTo(PaymentStatus.CANCELLED);
    }

    @Test
    @DisplayName("cancel() throws when payment is not in APPROVED status")
    void cancel_shouldThrow_whenNotApproved() {
        Payment payment = Payment.create(1L, 100L, BigDecimal.valueOf(10000), PaymentMethod.WALLET);

        assertThatThrownBy(payment::cancel)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Cannot cancel payment in status: REQUESTED");
    }

    @Test
    @DisplayName("fail() transitions REQUESTED to FAILED")
    void fail_shouldTransitionToFailed() {
        Payment payment = Payment.create(1L, 100L, BigDecimal.valueOf(10000), PaymentMethod.WALLET);

        payment.fail();

        assertThat(payment.getStatus()).isEqualTo(PaymentStatus.FAILED);
    }

    @Test
    @DisplayName("fail() throws when payment is not in REQUESTED status")
    void fail_shouldThrow_whenNotRequested() {
        Payment payment = Payment.create(1L, 100L, BigDecimal.valueOf(10000), PaymentMethod.CARD);
        payment.approve();

        assertThatThrownBy(payment::fail)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Cannot fail payment in status: APPROVED");
    }
}
