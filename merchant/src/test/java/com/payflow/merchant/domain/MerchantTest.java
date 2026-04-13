package com.payflow.merchant.domain;

import static com.payflow.merchant.domain.Merchant.*;
import static com.payflow.merchant.domain.MerchantFixture.*;
import static com.payflow.merchant.domain.enums.MerchantStatus.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * @description : 가맹점 도메인 테스트
 */
class MerchantTest {

	Merchant merchant;

	@BeforeEach
	void setUp() {
		MerchantRegisterRequest request = createMerchantRegisterRequest();
		this.merchant = register(request);
	}

	@Test
	@DisplayName("등록 - 정상")
	void 등록_정상() {
		assertEquals(PENDING, merchant.getStatus());
		assertNotNull(merchant.getRegisteredAt());
		assertEquals("포동동옷가게", merchant.getBusinessName());
	}

	@Test
	@DisplayName("등록 - 수수료율 유효성검증 실패")
	void 등록_수수료율_실패() {
		assertThrows(IllegalArgumentException.class,
			() -> register(createMerchantRegisterRequest(new BigDecimal("-1"))));

		assertThrows(IllegalArgumentException.class,
			() -> register(createMerchantRegisterRequest(new BigDecimal("101"))));
	}

	@Test
	@DisplayName("승인 - 성공")
	void 승인_성공() {
		merchant.activate();

		assertEquals(ACTIVE, merchant.getStatus());
		assertNotNull(merchant.getActivatedAt());
	}

	@Test
	@DisplayName("승인 - 실패 (PENDING 상태가 아님)")
	void 승인_실패() {
		merchant.activate();

		assertThrows(IllegalStateException.class, () -> merchant.activate());
	}

	@Test
	@DisplayName("정지 - 성공")
	void 정지_성공() {
		merchant.activate();

		merchant.suspend();

		assertEquals(SUSPENDED, merchant.getStatus());
		assertNotNull(merchant.getSuspendedAt());
	}

	@Test
	@DisplayName("정지 - 실패 (ACTIVE 상태가 아님)")
	void 정지_실패() {
		assertThrows(IllegalStateException.class, () -> merchant.suspend());
	}

	@Test
	@DisplayName("정지 해제 - 성공")
	void 정지해제_성공() {
		merchant.activate();
		merchant.suspend();

		merchant.resume();

		assertEquals(ACTIVE, merchant.getStatus());
		assertNull(merchant.getSuspendedAt());
	}

	@Test
	@DisplayName("정지 해제 - 실패 (SUSPENDED 상태가 아님)")
	void 정지해제_실패() {
		assertThrows(IllegalStateException.class, () -> merchant.resume());
	}

	@Test
	@DisplayName("계약 해지 - 성공 (ACTIVE)")
	void 계약해지_성공_ACTIVE() {
		merchant.activate();

		merchant.expire();

		assertEquals(EXPIRED, merchant.getStatus());
	}

	@Test
	@DisplayName("계약 해지 - 성공 (SUSPENDED)")
	void 계약해지_성공_SUSPENDED() {
		merchant.activate();
		merchant.suspend();

		merchant.expire();

		assertEquals(EXPIRED, merchant.getStatus());
		assertNull(merchant.getSuspendedAt());
	}

	@Test
	@DisplayName("계약 해지 - 실패 (PENDING)")
	void 계약해지_실패() {
		assertThrows(IllegalStateException.class, () -> merchant.expire());
	}

	@Test
	@DisplayName("활성 상태 확인")
	void 활성상태_확인() {
		assertFalse(merchant.isActive());

		merchant.activate();

		assertTrue(merchant.isActive());
	}

	@Test
	@DisplayName("수수료 계산")
	void 수수료_계산() {
		BigDecimal commission = merchant.getCommissionRate().calculate(new BigDecimal("100000"));

		assertEquals(new BigDecimal("3500"), commission);
	}
}
