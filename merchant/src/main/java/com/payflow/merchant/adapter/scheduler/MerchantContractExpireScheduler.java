package com.payflow.merchant.adapter.scheduler;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.payflow.merchant.application.provided.MerchantFinder;
import com.payflow.merchant.application.provided.MerchantModifier;
import com.payflow.merchant.domain.Merchant;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class MerchantContractExpireScheduler {

	private final MerchantFinder merchantFinder;
	private final MerchantModifier merchantModifier;
	private final RedissonClient redissonClient;

	@Scheduled(cron = "0 0 0 * * *")
	public void expireContracts() {
		RLock lock = redissonClient.getLock("scheduler:merchant-contract-expire");

		boolean acquired = false;
		try {
			acquired = lock.tryLock(0, 30, TimeUnit.MINUTES);
			if (!acquired) {
				log.info("[계약만료배치] 락 획득 실패 - 다른 인스턴스에서 실행 중");
				return;
			}

			LocalDate today = LocalDate.now();
			List<Merchant> merchants = merchantFinder.findExpirableContracts(today);
			log.info("[계약만료배치] 시작 - 기준일: {}, 대상: {}건", today, merchants.size());

			int failCount = 0;

			for (Merchant merchant : merchants) {
				try {
					merchantModifier.expire(merchant.getId());
				} catch (Exception e) {
					failCount++;
					log.error("[계약만료배치] 만료 처리 실패 - merchantId: {}, reason: {}",
						merchant.getId(),e.getMessage(), e);
				}
			}

			log.info("[계약만료배치] 완료 - 총: {}건, 실패: {}건", merchants.size(), failCount);

		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			log.error("[계약만료배치] 인터럽트 발생", e);
		} finally {
			if (acquired && lock.isHeldByCurrentThread()) {
				lock.unlock();
				log.debug("[계약만료배치] 락 해제 완료");
			}
		}
	}
}