package com.payflow.settlement.application.required;

import com.payflow.settlement.domain.Settlement;

public interface SettlementEventPublisher {

    void publishCompleted(Settlement settlement);
}
