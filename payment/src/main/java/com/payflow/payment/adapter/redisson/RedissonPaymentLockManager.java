package com.payflow.payment.adapter.redisson;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import com.payflow.payment.adapter.exception.PaymentLockException;
import com.payflow.payment.application.required.PaymentLockManager;

import lombok.RequiredArgsConstructor;

/**
 * @description   : redisson 기반 분산락 처리 adapter
 */
@Component
@RequiredArgsConstructor
public class RedissonPaymentLockManager implements PaymentLockManager {
	private final RedissonClient redissonClient;

	@Override
	public <T> T executeWithLock(Long memberId, Supplier<T> action) {
		RLock lock;
		try {
			lock = redissonClient.getLock("lock:payment:" + memberId);
		} catch (Exception e) {
			throw new RuntimeException("Redis 연결 실패", e);
		}

		boolean acquired = false;
		try {
			acquired = lock.tryLock(5, 10, TimeUnit.SECONDS);
			if (!acquired) {
				throw new PaymentLockException(memberId);
			}
			return action.get();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new RuntimeException("락 획득 중 인터럽트 발생", e);
		} finally {
			if (acquired && lock.isHeldByCurrentThread()) {
				lock.unlock();
			}
		}
	}
}
