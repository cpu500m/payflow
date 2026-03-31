package com.payflow.wallet.application;

import com.payflow.wallet.application.provided.ChargeWallet;
import com.payflow.wallet.application.provided.DeductBalance;
import com.payflow.wallet.application.provided.QueryBalance;
import com.payflow.wallet.application.required.TransactionLedger;
import com.payflow.wallet.application.required.WalletEventPublisher;
import com.payflow.wallet.application.required.WalletRepository;
import com.payflow.wallet.domain.InsufficientBalanceException;
import com.payflow.wallet.domain.Transaction;
import com.payflow.wallet.domain.TransactionType;
import com.payflow.wallet.domain.Wallet;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Transactional
public class WalletService implements ChargeWallet, DeductBalance, QueryBalance {

    private final WalletRepository walletRepository;
    private final TransactionLedger transactionLedger;
    private final WalletEventPublisher eventPublisher;

    @Override
    public WalletResponse charge(ChargeRequest request) {
        Wallet wallet = walletRepository.findByMemberId(request.memberId())
                .orElseGet(() -> walletRepository.save(new Wallet(request.memberId())));

        wallet.charge(request.amount());
        walletRepository.save(wallet);

        transactionLedger.save(Transaction.of(
                wallet.getId(),
                TransactionType.CHARGE,
                request.amount(),
                wallet.getBalance(),
                "Wallet charged"
        ));

        eventPublisher.publishCharged(wallet, request.amount());

        return new WalletResponse(wallet.getId(), wallet.getBalance());
    }

    @Override
    public DeductResult deduct(Long memberId, BigDecimal amount) {
        var walletOpt = walletRepository.findByMemberId(memberId);
        if (walletOpt.isEmpty()) {
            return DeductResult.WALLET_NOT_FOUND;
        }

        Wallet wallet = walletOpt.get();
        try {
            wallet.deduct(amount);
        } catch (InsufficientBalanceException e) {
            return DeductResult.INSUFFICIENT_BALANCE;
        }

        walletRepository.save(wallet);

        transactionLedger.save(Transaction.of(
                wallet.getId(),
                TransactionType.DEDUCT,
                amount,
                wallet.getBalance(),
                "Balance deducted"
        ));

        eventPublisher.publishDeducted(wallet, amount);

        return DeductResult.SUCCESS;
    }

    @Override
    @Transactional(readOnly = true)
    public WalletResponse getBalance(Long memberId) {
        Wallet wallet = walletRepository.findByMemberId(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Wallet not found for memberId: " + memberId));

        return new WalletResponse(wallet.getId(), wallet.getBalance());
    }

    public WalletResponse refund(Long memberId, BigDecimal amount) {
        Wallet wallet = walletRepository.findByMemberId(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Wallet not found for memberId: " + memberId));

        wallet.refund(amount);
        walletRepository.save(wallet);

        transactionLedger.save(Transaction.of(
                wallet.getId(),
                TransactionType.REFUND,
                amount,
                wallet.getBalance(),
                "Balance refunded"
        ));

        return new ChargeWallet.WalletResponse(wallet.getId(), wallet.getBalance());
    }
}
