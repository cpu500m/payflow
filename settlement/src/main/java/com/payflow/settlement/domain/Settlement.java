package com.payflow.settlement.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "settlements")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Settlement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long merchantId;

    @Column(nullable = false)
    private LocalDate settlementDate;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal totalAmount;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal commissionAmount;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal netAmount;

    @Column(nullable = false)
    private Integer transactionCount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SettlementStatus status;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private Settlement(Long merchantId, LocalDate settlementDate, BigDecimal totalAmount,
                       BigDecimal commissionAmount, BigDecimal netAmount,
                       int transactionCount, SettlementStatus status) {
        this.merchantId = merchantId;
        this.settlementDate = settlementDate;
        this.totalAmount = totalAmount;
        this.commissionAmount = commissionAmount;
        this.netAmount = netAmount;
        this.transactionCount = transactionCount;
        this.status = status;
        this.createdAt = LocalDateTime.now();
    }

    public static Settlement create(Long merchantId, LocalDate date,
                                    BigDecimal totalAmount, BigDecimal commissionAmount,
                                    int txCount) {
        BigDecimal netAmount = totalAmount.subtract(commissionAmount);
        return new Settlement(merchantId, date, totalAmount, commissionAmount,
                netAmount, txCount, SettlementStatus.CALCULATED);
    }

    public void confirm() {
        if (this.status != SettlementStatus.CALCULATED) {
            throw new IllegalStateException(
                    "Settlement can only be confirmed from CALCULATED status. Current: " + this.status);
        }
        this.status = SettlementStatus.CONFIRMED;
    }

    public void markAsPaid() {
        if (this.status != SettlementStatus.CONFIRMED) {
            throw new IllegalStateException(
                    "Settlement can only be marked as paid from CONFIRMED status. Current: " + this.status);
        }
        this.status = SettlementStatus.PAID;
    }
}
