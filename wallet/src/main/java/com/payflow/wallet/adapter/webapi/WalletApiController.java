package com.payflow.wallet.adapter.webapi;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.payflow.wallet.adapter.webapi.dto.WalletBalanceResponse;
import com.payflow.wallet.application.provided.WalletFinder;
import com.payflow.wallet.application.provided.WalletModifier;
import com.payflow.wallet.domain.Wallet;
import com.payflow.wallet.domain.dto.WalletBalanceRequest;
import com.payflow.wallet.domain.dto.WalletCreateRequest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * @description  : wallet api controller
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wallets")
public class WalletApiController {
	private final WalletFinder walletFinder;
	private final WalletModifier walletModifier;

	@PostMapping
	public ResponseEntity<Void> createWallet(@Valid @RequestBody WalletCreateRequest request) {
		walletModifier.create(request);

		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@GetMapping("/{memberId}/balance")
	public WalletBalanceResponse getBalance(@PathVariable Long memberId) {
		Long balance = walletFinder.getBalance(memberId);

		return WalletBalanceResponse.of(memberId, balance);
	}

	@PostMapping("/{memberId}/charge")
	public WalletBalanceResponse charge(@PathVariable Long memberId,
		@Valid @RequestBody WalletBalanceRequest request) {
		Wallet wallet = walletModifier.charge(memberId, request);

		return WalletBalanceResponse.of(memberId,wallet.getBalance());
	}

	@PostMapping("/{memberId}/deduct")
	public WalletBalanceResponse deduct(@PathVariable Long memberId,
		@Valid @RequestBody WalletBalanceRequest request) {
		Wallet wallet = walletModifier.deduct(memberId, request);

		return WalletBalanceResponse.of(memberId,wallet.getBalance());
	}

	@PostMapping("/{memberId}/refund")
	public WalletBalanceResponse refund(@PathVariable Long memberId,
		@Valid @RequestBody WalletBalanceRequest request) {
		Wallet wallet = walletModifier.refund(memberId, request);

		return WalletBalanceResponse.of(memberId,wallet.getBalance());
	}
}
