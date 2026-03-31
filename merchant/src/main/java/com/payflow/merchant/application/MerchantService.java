package com.payflow.merchant.application;

import com.payflow.merchant.application.provided.RegisterMerchant;
import com.payflow.merchant.application.required.MerchantRepository;
import com.payflow.merchant.domain.CommissionPolicy;
import com.payflow.merchant.domain.Merchant;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MerchantService implements RegisterMerchant {

    private final MerchantRepository merchantRepository;

    public MerchantService(MerchantRepository merchantRepository) {
        this.merchantRepository = merchantRepository;
    }

    @Override
    @Transactional
    public MerchantResponse register(MerchantRegisterRequest request) {
        if (merchantRepository.existsByBusinessNumber(request.businessNumber())) {
            throw new IllegalStateException("Business number already registered: " + request.businessNumber());
        }

        Merchant merchant = Merchant.create(request.name(), request.businessNumber());
        Merchant saved = merchantRepository.save(merchant);

        return toResponse(saved);
    }

    @Override
    public MerchantResponse findById(Long id) {
        Merchant merchant = merchantRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Merchant not found: " + id));

        return toResponse(merchant);
    }

    @Override
    public CommissionPolicy getCommissionPolicy(Long merchantId) {
        Merchant merchant = merchantRepository.findById(merchantId)
                .orElseThrow(() -> new IllegalArgumentException("Merchant not found: " + merchantId));

        return new CommissionPolicy(merchant.getCommissionRate());
    }

    private MerchantResponse toResponse(Merchant merchant) {
        return new MerchantResponse(
                merchant.getId(),
                merchant.getName(),
                merchant.getBusinessNumber(),
                merchant.getCommissionRate(),
                merchant.getStatus()
        );
    }
}
