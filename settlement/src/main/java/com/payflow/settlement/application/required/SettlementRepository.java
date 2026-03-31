package com.payflow.settlement.application.required;

import com.payflow.settlement.domain.Settlement;

import java.util.List;
import java.util.Optional;

public interface SettlementRepository {

    Settlement save(Settlement settlement);

    Optional<Settlement> findById(Long id);

    List<Settlement> findByMerchantId(Long merchantId);
}
