package com.payflow.payment.application;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.payflow.common.domain.PddPayException;
import com.payflow.payment.application.dto.NotificationEvent;
import com.payflow.payment.application.dto.PaymentEvent;
import com.payflow.payment.application.dto.WalletChangeRequest;
import com.payflow.payment.application.provided.PaymentFinder;
import com.payflow.payment.application.provided.PaymentModifier;
import com.payflow.payment.application.required.MemberClient;
import com.payflow.payment.application.required.MerchantClient;
import com.payflow.payment.application.required.PaymentLockManager;
import com.payflow.payment.application.required.PaymentRepository;
import com.payflow.payment.application.required.WalletClient;
import com.payflow.payment.domain.Payment;
import com.payflow.payment.domain.PaymentRequest;

import lombok.RequiredArgsConstructor;

/**
 * @description    :
 */
@Service
@RequiredArgsConstructor
@Transactional
@Validated
public class PaymentModifyService implements PaymentModifier {

	private final PaymentLockManager lockManager;

	private final PaymentFinder paymentFinder;

	private final PaymentRepository paymentRepository;

	private final MemberClient memberClient;
	private final MerchantClient merchantClient;
	private final WalletClient walletClient;

	private final ApplicationEventPublisher eventPublisher;

	@Override
	public Payment request(PaymentRequest request) {
		return lockManager.executeWithLock(request.memberId(), () -> {
			Payment payment = Payment.request(request);

			try {
				memberClient.validateActiveMember(request.memberId());

				merchantClient.validateActiveMerchant(request.merchantId());

				WalletChangeRequest deductRequest = WalletChangeRequest.of(request.merchantId(), request.amount());
				walletClient.deduct(deductRequest);

				payment.approve();
			} catch (PddPayException e) { // 비즈니스 예외일 경우엔 실패처리.
				payment.fail();
				paymentRepository.save(payment);
				return payment;
			}

			paymentRepository.save(payment);

			PaymentEvent paymentEvent = PaymentEvent.approved(payment);
			eventPublisher.publishEvent(paymentEvent);

			//todo 설정 설계 및 구현 필요
			NotificationEvent notificationEvent = NotificationEvent.approved(payment);
			eventPublisher.publishEvent(notificationEvent);

			return payment;
		});
	}

	@Override
	@Transactional
	public Payment cancel(Long paymentId) {
		Payment payment = paymentFinder.find(paymentId);

		payment.cancel();

		WalletChangeRequest changeRequest = WalletChangeRequest.of(payment.getMerchantId(), payment.getAmount());
		walletClient.refund(changeRequest);

		paymentRepository.save(payment);

		PaymentEvent paymentEvent = PaymentEvent.canceled(payment);
		eventPublisher.publishEvent(paymentEvent);

		NotificationEvent notificationEvent = NotificationEvent.canceled(payment);
		eventPublisher.publishEvent(notificationEvent);

		return payment;
	}
}
