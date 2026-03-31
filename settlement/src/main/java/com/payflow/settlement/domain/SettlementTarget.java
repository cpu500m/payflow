package com.payflow.settlement.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "settlement_targets")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SettlementTarget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long paymentId;

    @Column(nullable = false)
    private Long merchantId;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false, length = 20)
    private String eventType;

    @Column(nullable = false)
    private LocalDateTime eventAt;

    @Column(nullable = false)
    private boolean settled;

    public SettlementTarget(Long paymentId, Long merchantId, BigDecimal amount,
                            String eventType, LocalDateTime eventAt) {
        this.paymentId = paymentId;
        this.merchantId = merchantId;
        this.amount = amount;
        this.eventType = eventType;
        this.eventAt = eventAt;
        this.settled = false;
    }

    public void markAsSettled() {
        this.settled = true;
    }
}
