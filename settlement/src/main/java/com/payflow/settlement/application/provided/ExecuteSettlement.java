package com.payflow.settlement.application.provided;

import com.payflow.settlement.domain.Settlement;

import java.time.LocalDate;

public interface ExecuteSettlement {

    Settlement execute(Long merchantId, LocalDate date);
}
