package com.payflow.merchant.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "merchants")
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Merchant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String businessNumber;

    @Column(nullable = false, precision = 5, scale = 4)
    private BigDecimal commissionRate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MerchantStatus status;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private Merchant(String name, String businessNumber) {
        this.name = name;
        this.businessNumber = businessNumber;
        this.commissionRate = new BigDecimal("0.035");
        this.status = MerchantStatus.PENDING;
        this.createdAt = LocalDateTime.now();
    }

    public static Merchant create(String name, String businessNumber) {
        return new Merchant(name, businessNumber);
    }

    public void approve() {
        this.status = MerchantStatus.ACTIVE;
    }

    public void suspend() {
        this.status = MerchantStatus.SUSPENDED;
    }

    public void updateCommissionRate(BigDecimal rate) {
        if (rate == null || rate.compareTo(BigDecimal.ZERO) < 0 || rate.compareTo(BigDecimal.ONE) > 0) {
            throw new IllegalArgumentException("Commission rate must be between 0 and 1: " + rate);
        }
        this.commissionRate = rate;
    }
}
