package com.payflow.wallet.domain;

import static com.payflow.wallet.domain.WalletFixture.*;
import static org.junit.jupiter.api.Assertions.*;

import com.payflow.wallet.domain.exception.InSufficientBalanceException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * @description  : 지갑 엔티티 테스트
 */
class WalletTest {

	Wallet wallet;

	@BeforeEach
	void setUp() {
		this.wallet = createWallet();
	}

	@Test
	@DisplayName("생성 - 정상")
	void 생성_정상() throws Exception {
		assertEquals(1L, wallet.getMemberId());
		assertEquals(0L, wallet.getBalance());
		assertNotNull(wallet.getCreatedAt());
	}

	@Test
	@DisplayName("충전 - 정상")
	void 충전_정상() throws Exception {
		wallet.charge(10000L);

		assertEquals(10000L, wallet.getBalance());
	}

	@Test
	@DisplayName("충전 - 누적")
	void 충전_누적() throws Exception {
		wallet.charge(10000L);
		wallet.charge(5000L);

		assertEquals(15000L, wallet.getBalance());
	}

	@Test
	@DisplayName("충전 - 실패 (0원)")
	void 충전_실패_0원() throws Exception {
		assertThrows(IllegalStateException.class, () -> wallet.charge(0));
	}

	@Test
	@DisplayName("충전 - 실패 (음수)")
	void 충전_실패_음수() throws Exception {
		assertThrows(IllegalStateException.class, () -> wallet.charge(-1000));
	}

	@Test
	@DisplayName("차감 - 정상")
	void 차감_정상() throws Exception {
		wallet.charge(10000L);

		wallet.deduct(3000L);

		assertEquals(7000L, wallet.getBalance());
	}

	@Test
	@DisplayName("차감 - 전액 차감")
	void 차감_전액() throws Exception {
		wallet.charge(10000L);

		wallet.deduct(10000L);

		assertEquals(0L, wallet.getBalance());
	}

	@Test
	@DisplayName("차감 - 실패 (잔액 부족)")
	void 차감_실패_잔액부족() throws Exception {
		wallet.charge(5000L);

		assertThrows(InSufficientBalanceException.class, () -> wallet.deduct(10000L));
	}

	@Test
	@DisplayName("차감 - 실패 (0원)")
	void 차감_실패_0원() throws Exception {
		assertThrows(IllegalStateException.class, () -> wallet.deduct(0));
	}

	@Test
	@DisplayName("차감 - 실패 (음수)")
	void 차감_실패_음수() throws Exception {
		assertThrows(IllegalStateException.class, () -> wallet.deduct(-1000));
	}

	@Test
	@DisplayName("환불 - 정상")
	void 환불_정상() throws Exception {
		wallet.charge(10000L);
		wallet.deduct(3000L);

		wallet.refund(3000L);

		assertEquals(10000L, wallet.getBalance());
	}

	@Test
	@DisplayName("환불 - 실패 (0원)")
	void 환불_실패_0원() throws Exception {
		assertThrows(IllegalStateException.class, () -> wallet.refund(0));
	}

	@Test
	@DisplayName("환불 - 실패 (음수)")
	void 환불_실패_음수() throws Exception {
		assertThrows(IllegalStateException.class, () -> wallet.refund(-1000));
	}
}
