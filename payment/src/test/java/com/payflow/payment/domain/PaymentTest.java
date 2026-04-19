package com.payflow.payment.domain;

import static com.payflow.payment.domain.PaymentFixture.*;
import static com.payflow.payment.domain.PaymentStatus.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * @description  : 결제 도메인 테스트
 */
class PaymentTest {

	Payment payment;

	@BeforeEach
	void setUp() {
		this.payment = createRequestedPayment();
	}

	@Test
	@DisplayName("결제요청 - 정상")
	void 결제요청_정상() throws Exception {
		assertEquals(10000L, payment.getAmount());
		assertEquals(1L, payment.getMemberId());
		assertEquals(1L, payment.getMerchantId());
		assertEquals(REQUESTED, payment.getStatus());
		assertNotNull(payment.getCreatedAt());
	}

	@Test
	@DisplayName("결제요청 - 실패 (0원)")
	void 결제요청_실패_0원() throws Exception {
		PaymentRequest request = createPaymentRequest(0L);
		assertThrows(IllegalStateException.class, () -> Payment.request(request));
	}

	@Test
	@DisplayName("결제요청 - 실패 (음수)")
	void 결제요청_실패_음수() throws Exception {
		PaymentRequest request = createPaymentRequest(-1000L);
		assertThrows(IllegalStateException.class, () -> Payment.request(request));
	}

	@Test
	@DisplayName("승인 - 정상")
	void 승인_정상() throws Exception {
		payment.approve();

		assertEquals(APPROVED, payment.getStatus());
		assertNotNull(payment.getApprovedAt());
	}

	@Test
	@DisplayName("승인 - 실패 (이미 승인)")
	void 승인_실패_이미승인() throws Exception {
		payment.approve();

		assertThrows(IllegalStateException.class, () -> payment.approve());
	}

	@Test
	@DisplayName("승인 - 실패 (실패 상태)")
	void 승인_실패_실패상태() throws Exception {
		payment.fail();

		assertThrows(IllegalStateException.class, () -> payment.approve());
	}

	@Test
	@DisplayName("실패 - 정상")
	void 실패_정상() throws Exception {
		payment.fail();

		assertEquals(FAILED, payment.getStatus());
		assertNotNull(payment.getFailedAt());
	}

	@Test
	@DisplayName("실패 - 실패 (이미 승인)")
	void 실패_실패_이미승인() throws Exception {
		payment.approve();

		assertThrows(IllegalStateException.class, () -> payment.fail());
	}

	@Test
	@DisplayName("실패 - 실패 (이미 실패)")
	void 실패_실패_이미실패() throws Exception {
		payment.fail();

		assertThrows(IllegalStateException.class, () -> payment.fail());
	}

	@Test
	@DisplayName("취소 - 정상")
	void 취소_정상() throws Exception {
		payment.approve();

		payment.cancel();

		assertEquals(CANCELED, payment.getStatus());
		assertNotNull(payment.getCanceledAt());
	}

	@Test
	@DisplayName("취소 - 실패 (요청 상태)")
	void 취소_실패_요청상태() throws Exception {
		assertThrows(IllegalStateException.class, () -> payment.cancel());
	}

	@Test
	@DisplayName("취소 - 실패 (실패 상태)")
	void 취소_실패_실패상태() throws Exception {
		payment.fail();

		assertThrows(IllegalStateException.class, () -> payment.cancel());
	}

	@Test
	@DisplayName("취소 - 실패 (이미 취소)")
	void 취소_실패_이미취소() throws Exception {
		payment.approve();
		payment.cancel();

		assertThrows(IllegalStateException.class, () -> payment.cancel());
	}
}
