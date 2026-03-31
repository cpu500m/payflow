package com.payflow.settlement.application.provided;

import com.payflow.settlement.domain.SettlementStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface QuerySettlement {

    List<SettlementResponse> findByMerchantId(Long merchantId);

    Optional<SettlementResponse> findById(Long id);

    record SettlementResponse(
            Long id,
            Long merchantId,
            LocalDate date,
            BigDecimal totalAmount,
            BigDecimal commission,
            BigDecimal netAmount,
            int txCount,
            SettlementStatus status
    ) {}
}
