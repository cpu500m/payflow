package com.payflow.payment.application.provided;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RequestPaymentTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private PaymentEventPublisher paymentEventPublisher;

    @Mock
    private WalletClient walletClient;

    private PaymentService paymentService;

    @BeforeEach
    void setUp() {
        paymentService = new PaymentService(paymentRepository, paymentEventPublisher, walletClient);
    }

    @Test
    @DisplayName("WALLET payment succeeds when deduct returns SUCCESS")
    void requestWalletPayment_shouldApprove_whenDeductSucceeds() {
        // given
        PaymentRequest request = new PaymentRequest(1L, 100L, BigDecimal.valueOf(50000), PaymentMethod.WALLET);
        given(walletClient.deduct(eq(1L), eq(BigDecimal.valueOf(50000))))
                .willReturn(DeductResult.SUCCESS);
        given(paymentRepository.save(any(Payment.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        // when
        PaymentResponse response = paymentService.request(request);

        // then
        assertThat(response.status()).isEqualTo(PaymentStatus.APPROVED);
        assertThat(response.approvedAt()).isNotNull();
        verify(paymentEventPublisher).publishApproved(any(Payment.class));
        verify(paymentEventPublisher, never()).publishFailed(any(Payment.class));
    }

    @Test
    @DisplayName("WALLET payment fails when deduct returns INSUFFICIENT_BALANCE")
    void requestWalletPayment_shouldFail_whenInsufficientBalance() {
        // given
        PaymentRequest request = new PaymentRequest(1L, 100L, BigDecimal.valueOf(50000), PaymentMethod.WALLET);
        given(walletClient.deduct(eq(1L), eq(BigDecimal.valueOf(50000))))
                .willReturn(DeductResult.INSUFFICIENT_BALANCE);
        given(paymentRepository.save(any(Payment.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        // when
        PaymentResponse response = paymentService.request(request);

        // then
        assertThat(response.status()).isEqualTo(PaymentStatus.FAILED);
        assertThat(response.approvedAt()).isNull();
        verify(paymentEventPublisher).publishFailed(any(Payment.class));
        verify(paymentEventPublisher, never()).publishApproved(any(Payment.class));
    }

    @Test
    @DisplayName("CARD payment is approved immediately")
    void requestCardPayment_shouldApproveImmediately() {
        // given
        PaymentRequest request = new PaymentRequest(2L, 200L, BigDecimal.valueOf(30000), PaymentMethod.CARD);
        given(paymentRepository.save(any(Payment.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        // when
        PaymentResponse response = paymentService.request(request);

        // then
        assertThat(response.status()).isEqualTo(PaymentStatus.APPROVED);
        assertThat(response.approvedAt()).isNotNull();
        verify(walletClient, never()).deduct(any(), any());
        verify(paymentEventPublisher).publishApproved(any(Payment.class));
    }

    @Test
    @DisplayName("WALLET payment fails when wallet is not found")
    void requestWalletPayment_shouldFail_whenWalletNotFound() {
        // given
        PaymentRequest request = new PaymentRequest(999L, 100L, BigDecimal.valueOf(10000), PaymentMethod.WALLET);
        given(walletClient.deduct(eq(999L), eq(BigDecimal.valueOf(10000))))
                .willReturn(DeductResult.WALLET_NOT_FOUND);
        given(paymentRepository.save(any(Payment.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        // when
        PaymentResponse response = paymentService.request(request);

        // then
        assertThat(response.status()).isEqualTo(PaymentStatus.FAILED);
        verify(paymentEventPublisher).publishFailed(any(Payment.class));
    }
}
