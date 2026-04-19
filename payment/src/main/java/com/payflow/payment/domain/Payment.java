package com.payflow.payment.domain;

import static org.springframework.util.Assert.*;

import java.time.LocalDateTime;

import com.payflow.common.domain.AbstractEntity;

import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @description    : 결제 entity
 */

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Payment extends AbstractEntity {
	private Long amount;
	private PaymentStatus status;
	private Long memberId;
	private Long merchantId;
	private LocalDateTime createdAt;
	private LocalDateTime approvedAt;
	private LocalDateTime failedAt;
	private LocalDateTime canceledAt;

	public static Payment request(PaymentRequest request) {
		state(request.amount() > 0 , "[결제 요청 실패] 금액이 양수가 아닙니다.  amount : " + request.amount());
		Payment payment = new Payment();
		payment.amount = request.amount();
		payment.memberId = request.memberId();
		payment.merchantId = request.merchantId();

		payment.status = PaymentStatus.REQUESTED;
		payment.createdAt = LocalDateTime.now();

		return payment;
	}

	public void approve() {
		state(this.status == PaymentStatus.REQUESTED,
			"[결제 승인 에러] 승인 가능 상태가 아닙니다. status : " + this.status);

		this.status = PaymentStatus.APPROVED;
		this.approvedAt = LocalDateTime.now();
	}

	public void fail() {
		state(this.status == PaymentStatus.REQUESTED,
			"[결제 실패 에러] 실패 가능 상태가 아닙니다. status : " + this.status);

		this.status = PaymentStatus.FAILED;
		this.failedAt = LocalDateTime.now();
	}

	public void cancel() {
		state(this.status == PaymentStatus.APPROVED,
			"[결제 취소 에러] 취소 가능 상태가 아닙니다. status : " + this.status);

		this.status = PaymentStatus.CANCELED;
		this.canceledAt = LocalDateTime.now();
	}
}
