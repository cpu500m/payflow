package com.payflow.merchant.application.provided;

import com.payflow.merchant.domain.Merchant;
import com.payflow.merchant.domain.MerchantRegisterRequest;

import jakarta.validation.Valid;

/**
 * @description    :
 */
public interface MerchantModifier {

    Merchant register(@Valid MerchantRegisterRequest request);

    Merchant activate(Long merchantId);

    Merchant suspend(Long merchantId);

    Merchant resume(Long merchantId);

    Merchant expire(Long merchantId);
}
