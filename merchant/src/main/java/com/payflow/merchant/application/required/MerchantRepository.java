package com.payflow.merchant.application.required;

import com.payflow.merchant.domain.Merchant;

import java.util.Optional;

public interface MerchantRepository {

    Merchant save(Merchant merchant);

    Optional<Merchant> findById(Long id);

    boolean existsByBusinessNumber(String businessNumber);
}
