package com.subscriptionengine.productusage.domain.port.out;

import com.subscriptionengine.productusage.domain.model.UsageConsumption;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UsageConsumptionRepositoryPort {
    UsageConsumption save(UsageConsumption usageConsumption);
    List<UsageConsumption> saveAll(List<UsageConsumption> usageConsumptions);
    Optional<UsageConsumption> findById(String id);
    List<UsageConsumption> findAll();
    Page<UsageConsumption> findAll(Pageable pageable);
    List<UsageConsumption> findTotalUsageByProductId(String productId);
}
