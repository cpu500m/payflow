package com.payflow.merchant.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.payflow.merchant.application.provided.MerchantModifier;
import com.payflow.merchant.application.required.MerchantRepository;
import com.payflow.merchant.domain.Merchant;
import com.payflow.merchant.domain.MerchantRegisterRequest;

import lombok.RequiredArgsConstructor;

/**
 * @description    :
 */

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MerchantModifyService implements MerchantModifier {

	private final MerchantRepository merchantRepository;

	@Override
	public Merchant register(MerchantRegisterRequest request) {
		return null;
	}

	@Override
	public Merchant activate(Long merchantId) {
		return null;
	}

	@Override
	public Merchant deactivate(Long merchantId) {
		return null;
	}

	@Override
	public Merchant resume(Long merchantId) {
		return null;
	}

	@Override
	public Merchant expire(Long merchantId) {
		return null;
	}
}
