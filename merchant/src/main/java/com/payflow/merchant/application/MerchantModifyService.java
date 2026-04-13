package com.payflow.merchant.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.payflow.merchant.application.provided.MerchantFinder;
import com.payflow.merchant.application.provided.MerchantModifier;
import com.payflow.merchant.application.required.MerchantRepository;
import com.payflow.merchant.domain.Merchant;
import com.payflow.merchant.domain.MerchantRegisterRequest;
import com.payflow.merchant.domain.exception.DuplicateBusinessNameException;

import lombok.RequiredArgsConstructor;

/**
 * @description    :
 */

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Validated
public class MerchantModifyService implements MerchantModifier {

	private final MerchantRepository merchantRepository;
	private final MerchantFinder merchantFinder;

	@Override
	@Transactional
	public Merchant register(MerchantRegisterRequest request) {
		checkDuplicateBusinessName(request);

		Merchant merchant = Merchant.register(request);

		return merchantRepository.save(merchant);
	}

	@Override
	@Transactional
	public Merchant activate(Long merchantId) {
		Merchant merchant = merchantFinder.find(merchantId);

		merchant.activate();

		return merchantRepository.save(merchant);
	}

	@Override
	@Transactional
	public Merchant suspend(Long merchantId) {
		Merchant merchant = merchantFinder.find(merchantId);

		merchant.suspend();

		return merchantRepository.save(merchant);
	}

	@Override
	@Transactional
	public Merchant resume(Long merchantId) {
		Merchant merchant = merchantFinder.find(merchantId);

		merchant.resume();

		return merchantRepository.save(merchant);
	}

	@Override
	public Merchant expire(Long merchantId) {
		Merchant merchant = merchantFinder.find(merchantId);

		merchant.expire();

		return merchantRepository.save(merchant);
	}

	private void checkDuplicateBusinessName(MerchantRegisterRequest request) {
		if (merchantRepository.findByBusinessName(request.businessName()).isPresent()) {
			throw new DuplicateBusinessNameException(request.businessName());
		}
	}
}
