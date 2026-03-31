package com.payflow.merchant.adapter.in.web;

import com.payflow.merchant.application.provided.RegisterMerchant;
import com.payflow.merchant.application.provided.RegisterMerchant.MerchantRegisterRequest;
import com.payflow.merchant.application.provided.RegisterMerchant.MerchantResponse;
import com.payflow.merchant.domain.CommissionPolicy;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/merchants")
public class MerchantController {

    private final RegisterMerchant registerMerchant;

    public MerchantController(RegisterMerchant registerMerchant) {
        this.registerMerchant = registerMerchant;
    }

    @PostMapping
    public ResponseEntity<MerchantResponse> register(@RequestBody MerchantRegisterRequest request) {
        MerchantResponse response = registerMerchant.register(request);
        return ResponseEntity
                .created(URI.create("/api/v1/merchants/" + response.id()))
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MerchantResponse> findById(@PathVariable Long id) {
        MerchantResponse response = registerMerchant.findById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/commission-policy")
    public ResponseEntity<CommissionPolicy> getCommissionPolicy(@PathVariable Long id) {
        CommissionPolicy policy = registerMerchant.getCommissionPolicy(id);
        return ResponseEntity.ok(policy);
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<MerchantResponse> approve(@PathVariable Long id) {
        // For now, find and return - approval logic would be added to the port
        MerchantResponse response = registerMerchant.findById(id);
        return ResponseEntity.ok(response);
    }
}
