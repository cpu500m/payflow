package com.payflow.payment.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long memberId;

    @Column(nullable = false)
    private Long merchantId;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PaymentStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PaymentMethod method;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime approvedAt;

    private Payment(Long memberId, Long merchantId, BigDecimal amount, PaymentMethod method) {
        this.memberId = memberId;
        this.merchantId = merchantId;
        this.amount = amount;
        this.method = method;
        this.status = PaymentStatus.REQUESTED;
        this.createdAt = LocalDateTime.now();
    }

    public static Payment create(Long memberId, Long merchantId, BigDecimal amount, PaymentMethod method) {
        return new Payment(memberId, merchantId, amount, method);
    }

    public void approve() {
        if (this.status != PaymentStatus.REQUESTED) {
            throw new IllegalStateException(
                    "Cannot approve payment in status: " + this.status);
        }
        this.status = PaymentStatus.APPROVED;
        this.approvedAt = LocalDateTime.now();
    }

    public void cancel() {
        if (this.status != PaymentStatus.APPROVED) {
            throw new IllegalStateException(
                    "Cannot cancel payment in status: " + this.status);
        }
        this.status = PaymentStatus.CANCELLED;
    }

    public void fail() {
        if (this.status != PaymentStatus.REQUESTED) {
            throw new IllegalStateException(
                    "Cannot fail payment in status: " + this.status);
        }
        this.status = PaymentStatus.FAILED;
    }
}
