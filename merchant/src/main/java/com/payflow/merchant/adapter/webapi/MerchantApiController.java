package com.payflow.merchant.adapter.webapi;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.payflow.merchant.application.provided.MerchantFinder;
import com.payflow.merchant.application.provided.MerchantModifier;
import com.payflow.merchant.domain.MerchantRegisterRequest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * @description    :
 */
@RestController
@RequiredArgsConstructor
public class MerchantApiController {
	private final MerchantModifier merchantModifier;
	private final MerchantFinder merchantFinder;

	/*@PostMapping("/api/merchants")
	public MerchantRegisterResponse register(@RequestBody @Valid MerchantRegisterRequest request) {
		Merchant merchant = merchantModifier.register(request);

		return MerchantRegisterResponse.from(merchant);
	}*/
}
