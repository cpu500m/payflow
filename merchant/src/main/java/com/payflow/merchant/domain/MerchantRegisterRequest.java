package com.payflow.merchant.domain;

import java.math.BigDecimal;

/**
 * @description    :
 */
public record MerchantRegisterRequest(String businessName, BigDecimal commissionRate) {
}
