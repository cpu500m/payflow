package com.payflow.merchant.application.required;

import com.payflow.merchant.domain.Merchant;
import com.payflow.merchant.domain.enums.MerchantStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.Repository;

public interface MerchantRepository extends Repository<Merchant, Long> {

    Merchant save(Merchant merchant);

    Optional<Merchant> findById(Long id);

    Optional<Merchant> findByBusinessName(String businessName);

    List<Merchant> findByStatusInAndContractEndDateBefore(List<MerchantStatus> statuses, LocalDate date);
}
