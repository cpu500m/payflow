package com.payflow.settlement.application;

import com.payflow.settlement.application.provided.ExecuteSettlement;
import com.payflow.settlement.application.provided.QuerySettlement;
import com.payflow.settlement.application.required.MerchantClient;
import com.payflow.settlement.application.required.SettlementEventPublisher;
import com.payflow.settlement.application.required.SettlementRepository;
import com.payflow.settlement.application.required.SettlementTargetRepository;
import com.payflow.settlement.domain.CommissionPolicy;
import com.payflow.settlement.domain.Settlement;
import com.payflow.settlement.domain.SettlementTarget;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class SettlementService implements ExecuteSettlement, QuerySettlement {

    private final SettlementRepository settlementRepository;
    private final SettlementTargetRepository settlementTargetRepository;
    private final SettlementEventPublisher eventPublisher;
    private final MerchantClient merchantClient;

    public SettlementService(SettlementRepository settlementRepository,
                             SettlementTargetRepository settlementTargetRepository,
                             SettlementEventPublisher eventPublisher,
                             MerchantClient merchantClient) {
        this.settlementRepository = settlementRepository;
        this.settlementTargetRepository = settlementTargetRepository;
        this.eventPublisher = eventPublisher;
        this.merchantClient = merchantClient;
    }

    @Override
    @Transactional
    public Settlement execute(Long merchantId, LocalDate date) {
        List<SettlementTarget> targets =
                settlementTargetRepository.findUnsettledByMerchantIdAndDate(merchantId, date);

        if (targets.isEmpty()) {
            throw new IllegalStateException(
                    "No unsettled targets found for merchantId=" + merchantId + ", date=" + date);
        }

        BigDecimal totalAmount = targets.stream()
                .map(target -> {
                    if ("CANCELLED".equals(target.getEventType())) {
                        return target.getAmount().negate();
                    }
                    return target.getAmount();
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        CommissionPolicy policy = merchantClient.getCommissionPolicy(merchantId);
        BigDecimal commissionAmount = policy.calculate(totalAmount);

        Settlement settlement = Settlement.create(
                merchantId, date, totalAmount, commissionAmount, targets.size());

        Settlement saved = settlementRepository.save(settlement);

        List<Long> targetIds = targets.stream()
                .map(SettlementTarget::getId)
                .toList();
        settlementTargetRepository.markAsSettled(targetIds);

        eventPublisher.publishCompleted(saved);

        return saved;
    }

    @Override
    public List<SettlementResponse> findByMerchantId(Long merchantId) {
        return settlementRepository.findByMerchantId(merchantId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public Optional<SettlementResponse> findById(Long id) {
        return settlementRepository.findById(id)
                .map(this::toResponse);
    }

    private SettlementResponse toResponse(Settlement settlement) {
        return new SettlementResponse(
                settlement.getId(),
                settlement.getMerchantId(),
                settlement.getSettlementDate(),
                settlement.getTotalAmount(),
                settlement.getCommissionAmount(),
                settlement.getNetAmount(),
                settlement.getTransactionCount(),
                settlement.getStatus()
        );
    }
}
