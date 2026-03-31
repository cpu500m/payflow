package com.payflow.settlement.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CommissionPolicyTest {

    @Test
    @DisplayName("defaultPolicy should return 3.5% rate")
    void defaultPolicyShouldReturn3Point5Percent() {
        CommissionPolicy policy = CommissionPolicy.defaultPolicy();

        assertThat(policy.rate()).isEqualByComparingTo(new BigDecimal("0.035"));
    }

    @Test
    @DisplayName("calculate should compute commission correctly")
    void calculateShouldComputeCorrectly() {
        CommissionPolicy policy = CommissionPolicy.defaultPolicy();

        BigDecimal commission = policy.calculate(new BigDecimal("100000"));

        assertThat(commission).isEqualByComparingTo(new BigDecimal("3500.00"));
    }

    @Test
    @DisplayName("calculate should round to 2 decimal places with HALF_UP")
    void calculateShouldRoundCorrectly() {
        CommissionPolicy policy = new CommissionPolicy(new BigDecimal("0.033"));

        BigDecimal commission = policy.calculate(new BigDecimal("10001"));

        // 10001 * 0.033 = 330.033 -> rounds to 330.03
        assertThat(commission).isEqualByComparingTo(new BigDecimal("330.03"));
    }

    @Test
    @DisplayName("calculate with zero amount should return zero")
    void calculateWithZeroShouldReturnZero() {
        CommissionPolicy policy = CommissionPolicy.defaultPolicy();

        BigDecimal commission = policy.calculate(BigDecimal.ZERO);

        assertThat(commission).isEqualByComparingTo(new BigDecimal("0.00"));
    }

    @Test
    @DisplayName("should reject negative rate")
    void shouldRejectNegativeRate() {
        assertThatThrownBy(() -> new CommissionPolicy(new BigDecimal("-0.01")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("non-negative");
    }

    @Test
    @DisplayName("should reject null rate")
    void shouldRejectNullRate() {
        assertThatThrownBy(() -> new CommissionPolicy(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("calculate should reject null amount")
    void calculateShouldRejectNullAmount() {
        CommissionPolicy policy = CommissionPolicy.defaultPolicy();

        assertThatThrownBy(() -> policy.calculate(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("null");
    }
}
