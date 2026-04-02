package com.payflow.member.adapter.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.payflow.member.application.required.WalletClient;

import lombok.RequiredArgsConstructor;

/**
 * @description    :
 */
@RequiredArgsConstructor
@Component
public class WalletClientAdapter implements WalletClient {

	private final RestClient restClient;

	@Override
	public void createWallet(Long memberId) {
		restClient.post()
			.uri("/api/wallets")
			.body(new CreateWalletRequest(memberId))
			.retrieve()
			.toBodilessEntity();
	}

	@Override
	public long getWalletBalance(Long memberId) {
		Long balance = restClient.get()
			.uri("/api/wallets/{memberId}/balance", memberId)
			.retrieve()
			.body(Long.class);

		if (balance == null) {
			throw new IllegalStateException("지갑 잔액 조회 실패 - memberId: " + memberId);
		}

		return balance;
	}
}
