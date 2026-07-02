package com.subscriptionengine.productusage.domain.port.in;

import com.subscriptionengine.productusage.domain.model.UsageConsumption;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface UsageConsumptionUseCase {
    UsageConsumption createUsageConsumption(UsageConsumption usageConsumption);
    List<UsageConsumption> createUsageConsumptionBatch(List<UsageConsumption> usageConsumptions);
    List<UsageConsumption> getAllUsages();
}
