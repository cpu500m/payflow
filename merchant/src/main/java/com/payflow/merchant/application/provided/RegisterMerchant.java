package com.payflow.merchant.application.provided;

import com.payflow.merchant.domain.CommissionPolicy;
import com.payflow.merchant.domain.MerchantStatus;

import java.math.BigDecimal;

public interface RegisterMerchant {

    MerchantResponse register(MerchantRegisterRequest request);

    MerchantResponse findById(Long id);

    CommissionPolicy getCommissionPolicy(Long merchantId);

    record MerchantRegisterRequest(
            String name,
            String businessNumber
    ) {}

    record MerchantResponse(
            Long id,
            String name,
            String businessNumber,
            BigDecimal commissionRate,
            MerchantStatus status
    ) {}
}
