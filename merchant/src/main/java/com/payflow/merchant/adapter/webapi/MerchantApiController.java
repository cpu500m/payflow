package com.payflow.merchant.adapter.webapi;

import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.payflow.merchant.adapter.webapi.dto.MerchantResponse;
import com.payflow.merchant.application.provided.MerchantFinder;
import com.payflow.merchant.application.provided.MerchantModifier;
import com.payflow.merchant.domain.Merchant;
import com.payflow.merchant.domain.MerchantRegisterRequest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * @description    :
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/merchants")
public class MerchantApiController {
	private final MerchantModifier merchantModifier;
	private final MerchantFinder merchantFinder;

	public MerchantResponse register(@RequestBody @Valid MerchantRegisterRequest request) {
		Merchant merchant = merchantModifier.register(request);

		return MerchantResponse.from(merchant);
	}

	@PatchMapping("/{merchantId}/activate")
	public MerchantResponse activate(@PathVariable Long merchantId) {
		Merchant merchant = merchantModifier.activate(merchantId);

		return MerchantResponse.from(merchant);
	}

	@PatchMapping("/{merchantId}/suspend")
	public MerchantResponse suspend(@PathVariable Long merchantId) {
		Merchant merchant = merchantModifier.suspend(merchantId);

		return MerchantResponse.from(merchant);
	}

	@PatchMapping("/{merchantId}/resume")
	public MerchantResponse resume(@PathVariable Long merchantId) {
		Merchant merchant = merchantModifier.resume(merchantId);

		return MerchantResponse.from(merchant);
	}
}
