package com.payflow.merchant.domain;

import static lombok.AccessLevel.*;
import static org.springframework.util.Assert.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.payflow.common.domain.AbstractEntity;
import com.payflow.merchant.domain.enums.MerchantStatus;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * description : 가맹점 entity
 */
@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
public class Merchant extends AbstractEntity {

    private String businessName;

    private CommissionRate commissionRate;

    private MerchantStatus status;

    private LocalDateTime registeredAt;

    private LocalDateTime activatedAt;

    private LocalDate contractStartDate;

    private LocalDate contractEndDate;

    private LocalDateTime suspendedAt;

    public static Merchant register(MerchantRegisterRequest request) {
        Merchant merchant = new Merchant();
        merchant.businessName = request.businessName();
        merchant.commissionRate = new CommissionRate(request.commissionRate());

        merchant.status = MerchantStatus.PENDING;
        merchant.registeredAt = LocalDateTime.now();

        return merchant;
    }

    public void activate() {
        state(this.status == MerchantStatus.PENDING, "[가맹점 승인 에러] PENDING 상태가 아닙니다.");

        this.status = MerchantStatus.ACTIVE;
        this.activatedAt = LocalDateTime.now();
    }

    public void suspend() {
        state(this.status == MerchantStatus.ACTIVE, "[가맹점 정지 에러] ACTIVE 상태가 아닙니다.");

        this.status = MerchantStatus.SUSPENDED;
        this.suspendedAt = LocalDateTime.now();
    }

    public void resume() {
        state(this.status == MerchantStatus.SUSPENDED, "[가맹점 정지 해제 에러] SUSPENDED 상태가 아닙니다.");

        this.status = MerchantStatus.ACTIVE;
        this.suspendedAt = null;
    }

    public void expire() {
        state(List.of(MerchantStatus.ACTIVE, MerchantStatus.SUSPENDED).contains(this.status),
            "[가맹점 계약 해지 에러] 해지 가능 상태가 아닙니다. 현재 상태 : " + this.status);

        this.status = MerchantStatus.EXPIRED;
        this.suspendedAt = null;
    }

    public boolean isActive() {
        return this.status == MerchantStatus.ACTIVE;
    }
}
