package com.payflow.merchant.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MerchantTest {

    @Test
    @DisplayName("Merchant.create sets PENDING status and default commission rate 3.5%")
    void create() {
        Merchant merchant = Merchant.create("PayCo", "123-45-67890");

        assertThat(merchant.getName()).isEqualTo("PayCo");
        assertThat(merchant.getBusinessNumber()).isEqualTo("123-45-67890");
        assertThat(merchant.getCommissionRate()).isEqualByComparingTo(new BigDecimal("0.035"));
        assertThat(merchant.getStatus()).isEqualTo(MerchantStatus.PENDING);
        assertThat(merchant.getCreatedAt()).isNotNull();
    }

    @Test
    @DisplayName("approve changes status to ACTIVE")
    void approve() {
        Merchant merchant = Merchant.create("PayCo", "123-45-67890");

        merchant.approve();

        assertThat(merchant.getStatus()).isEqualTo(MerchantStatus.ACTIVE);
    }

    @Test
    @DisplayName("suspend changes status to SUSPENDED")
    void suspend() {
        Merchant merchant = Merchant.create("PayCo", "123-45-67890");
        merchant.approve();

        merchant.suspend();

        assertThat(merchant.getStatus()).isEqualTo(MerchantStatus.SUSPENDED);
    }

    @Test
    @DisplayName("updateCommissionRate updates the rate")
    void updateCommissionRate() {
        Merchant merchant = Merchant.create("PayCo", "123-45-67890");

        merchant.updateCommissionRate(new BigDecimal("0.05"));

        assertThat(merchant.getCommissionRate()).isEqualByComparingTo(new BigDecimal("0.05"));
    }

    @Test
    @DisplayName("updateCommissionRate rejects negative rate")
    void updateCommissionRateRejectsNegative() {
        Merchant merchant = Merchant.create("PayCo", "123-45-67890");

        assertThatThrownBy(() -> merchant.updateCommissionRate(new BigDecimal("-0.01")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Commission rate must be between 0 and 1");
    }

    @Test
    @DisplayName("updateCommissionRate rejects rate above 1")
    void updateCommissionRateRejectsAboveOne() {
        Merchant merchant = Merchant.create("PayCo", "123-45-67890");

        assertThatThrownBy(() -> merchant.updateCommissionRate(new BigDecimal("1.5")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Commission rate must be between 0 and 1");
    }

    @Test
    @DisplayName("CommissionPolicy calculates commission correctly")
    void commissionPolicyCalculation() {
        CommissionPolicy policy = new CommissionPolicy(new BigDecimal("0.035"));

        BigDecimal commission = policy.calculate(new BigDecimal("100000"));

        assertThat(commission).isEqualByComparingTo(new BigDecimal("3500"));
    }
}
