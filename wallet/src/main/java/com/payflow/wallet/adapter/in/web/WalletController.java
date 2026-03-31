package com.payflow.wallet.adapter.in.web;

import com.payflow.wallet.application.WalletService;
import com.payflow.wallet.application.provided.ChargeWallet;
import com.payflow.wallet.application.provided.ChargeWallet.ChargeRequest;
import com.payflow.wallet.application.provided.ChargeWallet.WalletResponse;
import com.payflow.wallet.application.provided.DeductBalance;
import com.payflow.wallet.application.provided.DeductBalance.DeductResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1/wallets")
@RequiredArgsConstructor
public class WalletController {

    private final ChargeWallet chargeWallet;
    private final DeductBalance deductBalance;
    private final WalletService walletService;

    @PostMapping("/charge")
    public ResponseEntity<WalletResponse> charge(@RequestBody ChargeRequest request) {
        WalletResponse response = chargeWallet.charge(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/deduct")
    public ResponseEntity<DeductResult> deduct(@RequestBody DeductRequest request) {
        DeductResult result = deductBalance.deduct(request.memberId(), request.amount());
        return switch (result) {
            case SUCCESS -> ResponseEntity.ok(result);
            case INSUFFICIENT_BALANCE -> ResponseEntity.unprocessableEntity().body(result);
            case WALLET_NOT_FOUND -> ResponseEntity.notFound().build();
        };
    }

    @PostMapping("/refund")
    public ResponseEntity<WalletResponse> refund(@RequestBody RefundRequest request) {
        WalletResponse response = walletService.refund(request.memberId(), request.amount());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{memberId}/balance")
    public ResponseEntity<WalletResponse> getBalance(@PathVariable Long memberId) {
        WalletResponse response = walletService.getBalance(memberId);
        return ResponseEntity.ok(response);
    }

    record DeductRequest(Long memberId, BigDecimal amount) {
    }

    record RefundRequest(Long memberId, BigDecimal amount) {
    }
}
