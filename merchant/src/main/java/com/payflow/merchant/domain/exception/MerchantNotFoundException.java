package com.payflow.merchant.domain.exception;

import org.springframework.http.HttpStatus;

import com.payflow.common.domain.PddPayException;

public class MerchantNotFoundException extends PddPayException {

    private static final String MESSAGE = "해당 id의 가맹점이 존재하지 않습니다. ";

    public MerchantNotFoundException(Long memberId) {
        super(MESSAGE + memberId);
    }

    @Override
    public HttpStatus getStatusCode() {
        return HttpStatus.NOT_FOUND;
    }
}
