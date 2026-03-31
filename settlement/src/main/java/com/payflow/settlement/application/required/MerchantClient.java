package com.payflow.settlement.application.required;

import com.payflow.settlement.domain.CommissionPolicy;

public interface MerchantClient {

    CommissionPolicy getCommissionPolicy(Long merchantId);
}
