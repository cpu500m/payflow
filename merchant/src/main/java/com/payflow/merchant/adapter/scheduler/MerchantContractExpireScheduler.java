package com.payflow.merchant.adapter.scheduler;

import java.time.LocalDate;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.payflow.merchant.application.provided.MerchantFinder;
import com.payflow.merchant.application.provided.MerchantModifier;
import com.payflow.merchant.domain.Merchant;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;

@Slf4j
@Component
@RequiredArgsConstructor
public class MerchantContractExpireScheduler {

	private final MerchantFinder merchantFinder;
	private final MerchantModifier merchantModifier;

	@Scheduled(cron = "0 0 0 * * *")
	@SchedulerLock(name = "merchantContractExpire", lockAtLeastFor = "PT5M", lockAtMostFor = "PT30M")
	public void expireContracts() {
		LocalDate today = LocalDate.now();
		List<Merchant> merchants = merchantFinder.findExpirableContracts(today);

		log.info("계약 만료 배치 시작 - 대상: {}건", merchants.size());

		int successCount = 0;
		int failCount = 0;

		for (Merchant merchant : merchants) {
			try {
				merchantModifier.expire(merchant.getId());
				successCount++;
			} catch (Exception e) {
				failCount++;
				log.error("가맹점 만료 처리 실패 - merchantId: {}, reason: {}", merchant.getId(), e.getMessage(), e);
			}
		}

		log.info("계약 만료 배치 완료 - 성공: {}건, 실패: {}건", successCount, failCount);
	}
}
