package com.payflow.settlement.application.required;

import com.payflow.settlement.domain.SettlementTarget;

import java.time.LocalDate;
import java.util.List;

public interface SettlementTargetRepository {

    SettlementTarget save(SettlementTarget target);

    List<SettlementTarget> findUnsettledByMerchantIdAndDate(Long merchantId, LocalDate date);

    void markAsSettled(List<Long> ids);
}
