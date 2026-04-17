package com.payflow.merchant.adapter.webapi.dto;

import com.payflow.merchant.domain.Merchant;

/**
 * @description    :
 */
public record MerchantResponse(Long merchantId, String businessName, String status) {
	public static MerchantResponse from(Merchant merchant) {
		return new MerchantResponse(
			merchant.getId(),
			merchant.getBusinessName(),
			merchant.getStatus().name()
		);
	}
}
