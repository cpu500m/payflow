package com.payflow.wallet.domain;

import static org.springframework.util.Assert.*;

import java.time.LocalDateTime;

import com.payflow.common.domain.AbstractEntity;
import com.payflow.wallet.domain.exception.InSufficientBalanceException;

import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @description  : 지갑 entity
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Wallet extends AbstractEntity {
	private Long memberId;

	private Long balance;

	private LocalDateTime createdAt;

	public static Wallet create(WalletRegisterRequest registerRequest) {
		Wallet wallet = new Wallet();
		wallet.memberId = registerRequest.memberId();

		wallet.createdAt = LocalDateTime.now();
		wallet.balance = 0L;

		return wallet;
	}

	public void charge(long amount) {
		validateAmount(amount,"[지갑 충전 실패] 충전 금액이 양수가 아닙니다.");

		this.balance += amount;
	}

	public void deduct(long amount) {
		validateAmount(amount, "[지갑 차감 실패] 차감 금액이 양수가 아닙니다.");
		if (this.balance - amount < 0) {
			throw new InSufficientBalanceException(balance);
		}

		this.balance -= amount;
	}

	public void refund(long amount) {
		validateAmount(amount,"[지갑 환불 실패] 환불 금액이 양수가 아닙니다.");

		this.balance += amount;
	}

	private static void validateAmount(long amount, String message) {
		state(amount > 0,  message);
	}
}
