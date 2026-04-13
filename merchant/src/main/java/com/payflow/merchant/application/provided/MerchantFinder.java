package com.payflow.merchant.application.provided;

import com.payflow.merchant.domain.CommissionRate;
import com.payflow.merchant.domain.Merchant;

/**
 * @description    :
 */
public interface MerchantFinder {
	Merchant find(Long merchantId);

	CommissionRate getCommissionRate(Long merchantId);
}
