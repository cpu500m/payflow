package com.payflow.settlement.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SettlementTest {

    @Nested
    @DisplayName("create()")
    class Create {

        @Test
        @DisplayName("should create settlement with CALCULATED status and computed net amount")
        void shouldCreateWithCalculatedStatusAndNetAmount() {
            BigDecimal totalAmount = new BigDecimal("100000");
            BigDecimal commissionAmount = new BigDecimal("3500");

            Settlement settlement = Settlement.create(1L, LocalDate.of(2026, 3, 31),
                    totalAmount, commissionAmount, 10);

            assertThat(settlement.getMerchantId()).isEqualTo(1L);
            assertThat(settlement.getSettlementDate()).isEqualTo(LocalDate.of(2026, 3, 31));
            assertThat(settlement.getTotalAmount()).isEqualByComparingTo(totalAmount);
            assertThat(settlement.getCommissionAmount()).isEqualByComparingTo(commissionAmount);
            assertThat(settlement.getNetAmount()).isEqualByComparingTo(new BigDecimal("96500"));
            assertThat(settlement.getTransactionCount()).isEqualTo(10);
            assertThat(settlement.getStatus()).isEqualTo(SettlementStatus.CALCULATED);
            assertThat(settlement.getCreatedAt()).isNotNull();
        }
    }

    @Nested
    @DisplayName("confirm()")
    class Confirm {

        @Test
        @DisplayName("should transition from CALCULATED to CONFIRMED")
        void shouldTransitionToConfirmed() {
            Settlement settlement = Settlement.create(1L, LocalDate.now(),
                    new BigDecimal("100000"), new BigDecimal("3500"), 5);

            settlement.confirm();

            assertThat(settlement.getStatus()).isEqualTo(SettlementStatus.CONFIRMED);
        }

        @Test
        @DisplayName("should throw when confirming from non-CALCULATED status")
        void shouldThrowWhenNotCalculated() {
            Settlement settlement = Settlement.create(1L, LocalDate.now(),
                    new BigDecimal("100000"), new BigDecimal("3500"), 5);
            settlement.confirm();

            assertThatThrownBy(settlement::confirm)
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("CALCULATED");
        }
    }

    @Nested
    @DisplayName("markAsPaid()")
    class MarkAsPaid {

        @Test
        @DisplayName("should transition from CONFIRMED to PAID")
        void shouldTransitionToPaid() {
            Settlement settlement = Settlement.create(1L, LocalDate.now(),
                    new BigDecimal("100000"), new BigDecimal("3500"), 5);
            settlement.confirm();

            settlement.markAsPaid();

            assertThat(settlement.getStatus()).isEqualTo(SettlementStatus.PAID);
        }

        @Test
        @DisplayName("should throw when marking as paid from non-CONFIRMED status")
        void shouldThrowWhenNotConfirmed() {
            Settlement settlement = Settlement.create(1L, LocalDate.now(),
                    new BigDecimal("100000"), new BigDecimal("3500"), 5);

            assertThatThrownBy(settlement::markAsPaid)
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("CONFIRMED");
        }
    }
}
