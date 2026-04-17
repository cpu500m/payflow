package com.payflow.wallet.domain;

import static com.payflow.wallet.domain.WalletFixture.*;
import static com.payflow.wallet.domain.enums.TransactionType.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * @description  : 거래내역 entity test
 */
class WalletTransactionTest {

	@Test
	@DisplayName("생성 - 충전")
	void 생성_충전() throws Exception {
		Wallet wallet = createWalletWithBalance(10000L);
		WalletTransactionCreateRequest request = createTransactionRequest(CHARGE, 10000L);

		WalletTransaction transaction = WalletTransaction.create(wallet, request);

		assertEquals(CHARGE, transaction.getType());
		assertEquals(10000L, transaction.getAmount());
		assertEquals("CHARGE 거래", transaction.getDescription());
		assertEquals(wallet, transaction.getWallet());
	}

	@Test
	@DisplayName("생성 - 차감")
	void 생성_차감() throws Exception {
		Wallet wallet = createWalletWithBalance(10000L);
		wallet.deduct(3000L);
		WalletTransactionCreateRequest request = createTransactionRequest(DEDUCT, 3000L);

		WalletTransaction transaction = WalletTransaction.create(wallet, request);

		assertEquals(DEDUCT, transaction.getType());
		assertEquals(3000L, transaction.getAmount());
	}

	@Test
	@DisplayName("생성 - 환불")
	void 생성_환불() throws Exception {
		Wallet wallet = createWalletWithBalance(5000L);
		WalletTransactionCreateRequest request = createTransactionRequest(REFUND, 2000L);

		WalletTransaction transaction = WalletTransaction.create(wallet, request);

		assertEquals(REFUND, transaction.getType());
		assertEquals(2000L, transaction.getAmount());
	}

	@Test
	@DisplayName("생성 - balanceAfter는 생성 시점의 지갑 잔액")
	void 생성_balanceAfter_반영() throws Exception {
		Wallet wallet = createWalletWithBalance(10000L);
		wallet.deduct(4000L);
		WalletTransactionCreateRequest request = createTransactionRequest(DEDUCT, 4000L);

		WalletTransaction transaction = WalletTransaction.create(wallet, request);

		assertEquals(6000L, transaction.getBalanceAfter());
	}

	@Test
	@DisplayName("생성 - createdAt 자동 설정")
	void 생성_createdAt_설정() throws Exception {
		Wallet wallet = createWallet();
		WalletTransactionCreateRequest request = createTransactionRequest(CHARGE, 1000L);

		WalletTransaction transaction = WalletTransaction.create(wallet, request);

		assertNotNull(transaction.getCreatedAt());
	}

	@Test
	@DisplayName("생성 - 지갑 연관관계 설정")
	void 생성_지갑_연관관계() throws Exception {
		Wallet wallet = createWalletWithBalance(10000L);
		WalletTransactionCreateRequest request = createTransactionRequest(CHARGE, 10000L);

		WalletTransaction transaction = WalletTransaction.create(wallet, request);

		assertSame(wallet, transaction.getWallet());
	}
}
