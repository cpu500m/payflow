package com.payflow.wallet.application.required;

import com.payflow.wallet.domain.Transaction;

import java.util.List;

public interface TransactionLedger {

    Transaction save(Transaction transaction);

    List<Transaction> findByWalletId(Long walletId);
}
