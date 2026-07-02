package com.subscriptionengine.productusage.domain.port.in;

import com.subscriptionengine.productusage.domain.model.UsageConsumption;

public interface UsageValidationService {
    void validateUsage(UsageConsumption usageConsumption);
}
