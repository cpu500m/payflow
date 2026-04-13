package com.payflow.merchant.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.payflow.merchant.application.provided.MerchantFinder;
import com.payflow.merchant.application.required.MerchantRepository;
import com.payflow.merchant.domain.CommissionRate;
import com.payflow.merchant.domain.Merchant;

import lombok.RequiredArgsConstructor;

/**
 * @description    :
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MerchantQueryService implements MerchantFinder {

	private final MerchantRepository merchantRepository;

	@Override
	public Merchant find(Long merchantId) {
		return null;
	}

	@Override
	public CommissionRate getCommissionRate(Long merchantId) {
		return null;
	}
}
