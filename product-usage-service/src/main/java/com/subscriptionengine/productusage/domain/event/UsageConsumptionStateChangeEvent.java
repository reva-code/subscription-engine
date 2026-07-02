package com.subscriptionengine.productusage.domain.event;

import com.subscriptionengine.productusage.domain.model.UsageConsumption;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UsageConsumptionStateChangeEvent {
    private final UsageConsumption usageConsumption;
    private final String oldState;
    private final String newState;
}
