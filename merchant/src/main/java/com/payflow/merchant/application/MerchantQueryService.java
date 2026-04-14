package com.payflow.merchant.application;

import com.payflow.merchant.domain.exception.MerchantNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.payflow.merchant.application.provided.MerchantFinder;
import com.payflow.merchant.application.required.MerchantRepository;
import com.payflow.merchant.domain.CommissionRate;
import com.payflow.merchant.domain.Merchant;

import lombok.RequiredArgsConstructor;

/**
 * @description : 가맹점 정보 조회 application service
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MerchantQueryService implements MerchantFinder {

	private final MerchantRepository merchantRepository;

	@Override
	public Merchant find(Long merchantId) {
        return merchantRepository.findById(merchantId)
                .orElseThrow(() -> new MerchantNotFoundException(merchantId));
	}

	@Override
	public CommissionRate getCommissionRate(Long merchantId) {
        Merchant merchant = find(merchantId);

        return merchant.getCommissionRate();
    }
}
