package com.payflow.merchant.application.provided;

import com.payflow.merchant.domain.CommissionRate;
import com.payflow.merchant.domain.Merchant;

import java.time.LocalDate;
import java.util.List;

/**
 * @description    :
 */
public interface MerchantFinder {
	Merchant find(Long merchantId);

	CommissionRate getCommissionRate(Long merchantId);

	List<Merchant> findExpirableContracts(LocalDate referenceDate);
}
