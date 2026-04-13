package com.payflow.merchant.domain;

import java.math.BigDecimal;

/**
 * @description : 가맹점 도메인 test fixture
 */
public class MerchantFixture {

	public static MerchantRegisterRequest createMerchantRegisterRequest() {
		return new MerchantRegisterRequest("포동동옷가게", new BigDecimal("3.5"));
	}

	public static MerchantRegisterRequest createMerchantRegisterRequest(BigDecimal commissionRate) {
		return new MerchantRegisterRequest("포동동옷가게", commissionRate);
	}
}
