package com.payflow.wallet.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;

class WalletTest {

    @Nested
    @DisplayName("charge")
    class Charge {

        @Test
        @DisplayName("should increase balance by the charged amount")
        void shouldIncreaseBalance() {
            Wallet wallet = new Wallet(1L);

            wallet.charge(new BigDecimal("10000"));

            assertThat(wallet.getBalance()).isEqualByComparingTo(new BigDecimal("10000"));
        }

        @Test
        @DisplayName("should accumulate multiple charges")
        void shouldAccumulateCharges() {
            Wallet wallet = new Wallet(1L);

            wallet.charge(new BigDecimal("10000"));
            wallet.charge(new BigDecimal("5000"));

            assertThat(wallet.getBalance()).isEqualByComparingTo(new BigDecimal("15000"));
        }

        @Test
        @DisplayName("should reject zero or negative amount")
        void shouldRejectNonPositiveAmount() {
            Wallet wallet = new Wallet(1L);

            assertThatThrownBy(() -> wallet.charge(BigDecimal.ZERO))
                    .isInstanceOf(IllegalArgumentException.class);
            assertThatThrownBy(() -> wallet.charge(new BigDecimal("-1000")))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("deduct")
    class Deduct {

        @Test
        @DisplayName("should decrease balance by the deducted amount")
        void shouldDecreaseBalance() {
            Wallet wallet = new Wallet(1L);
            wallet.charge(new BigDecimal("10000"));

            wallet.deduct(new BigDecimal("3000"));

            assertThat(wallet.getBalance()).isEqualByComparingTo(new BigDecimal("7000"));
        }

        @Test
        @DisplayName("should allow deducting the entire balance")
        void shouldAllowDeductingEntireBalance() {
            Wallet wallet = new Wallet(1L);
            wallet.charge(new BigDecimal("10000"));

            wallet.deduct(new BigDecimal("10000"));

            assertThat(wallet.getBalance()).isEqualByComparingTo(BigDecimal.ZERO);
        }

        @Test
        @DisplayName("should reject zero or negative amount")
        void shouldRejectNonPositiveAmount() {
            Wallet wallet = new Wallet(1L);
            wallet.charge(new BigDecimal("10000"));

            assertThatThrownBy(() -> wallet.deduct(BigDecimal.ZERO))
                    .isInstanceOf(IllegalArgumentException.class);
            assertThatThrownBy(() -> wallet.deduct(new BigDecimal("-1000")))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("insufficient balance")
    class InsufficientBalance {

        @Test
        @DisplayName("should throw InsufficientBalanceException when balance is not enough")
        void shouldThrowWhenInsufficientBalance() {
            Wallet wallet = new Wallet(1L);
            wallet.charge(new BigDecimal("5000"));

            assertThatThrownBy(() -> wallet.deduct(new BigDecimal("10000")))
                    .isInstanceOf(InsufficientBalanceException.class)
                    .hasMessageContaining("5000")
                    .hasMessageContaining("10000");
        }

        @Test
        @DisplayName("should throw when deducting from zero balance")
        void shouldThrowWhenDeductingFromZeroBalance() {
            Wallet wallet = new Wallet(1L);

            assertThatThrownBy(() -> wallet.deduct(new BigDecimal("1")))
                    .isInstanceOf(InsufficientBalanceException.class);
        }
    }

    @Nested
    @DisplayName("refund")
    class Refund {

        @Test
        @DisplayName("should increase balance by the refunded amount")
        void shouldIncreaseBalance() {
            Wallet wallet = new Wallet(1L);
            wallet.charge(new BigDecimal("10000"));
            wallet.deduct(new BigDecimal("3000"));

            wallet.refund(new BigDecimal("3000"));

            assertThat(wallet.getBalance()).isEqualByComparingTo(new BigDecimal("10000"));
        }

        @Test
        @DisplayName("should reject zero or negative amount")
        void shouldRejectNonPositiveAmount() {
            Wallet wallet = new Wallet(1L);

            assertThatThrownBy(() -> wallet.refund(BigDecimal.ZERO))
                    .isInstanceOf(IllegalArgumentException.class);
            assertThatThrownBy(() -> wallet.refund(new BigDecimal("-1000")))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
