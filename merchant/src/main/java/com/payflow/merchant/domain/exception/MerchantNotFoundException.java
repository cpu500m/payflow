package com.payflow.merchant.domain.exception;

public class MerchantNotFoundException extends RuntimeException {

    private static final String MESSAGE = "해당 id의 가맹점이 존재하지 않습니다. ";

    public MerchantNotFoundException(Long memberId) {
        super(MESSAGE + memberId);
    }
}
