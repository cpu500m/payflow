package com.payflow.wallet.application.required;

import com.payflow.wallet.domain.Wallet;

import java.util.Optional;

public interface WalletRepository {

    Wallet save(Wallet wallet);

    Optional<Wallet> findByMemberId(Long memberId);

    Optional<Wallet> findById(Long id);
}
