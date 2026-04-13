package com.payflow.member;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import com.payflow.member.application.required.WalletClient;

/**
 * @description    :
 */
@TestConfiguration
public class MemberTestConfiguration {

	@Bean
	public WalletClient walletClient() {
		return new WalletClient() {
			@Override
			public void createWallet(Long memberId) {
				System.out.println("지갑 생성 요청 - memberId: " + memberId);
			}

			@Override
			public long getWalletBalance(Long memberId) {
				return 0L;
			}
		};
	}

}
