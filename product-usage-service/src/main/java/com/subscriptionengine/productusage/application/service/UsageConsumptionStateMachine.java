package com.subscriptionengine.productusage.application.service;

import com.subscriptionengine.productusage.domain.model.UsageConsumption;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import com.subscriptionengine.productusage.domain.event.UsageConsumptionStateChangeEvent;

@Component
public class UsageConsumptionStateMachine {

    private final ApplicationEventPublisher eventPublisher;

    public UsageConsumptionStateMachine(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public void process(UsageConsumption usageConsumption) {
        String oldState = usageConsumption.getStatus();
        
        if (oldState == null || oldState.equals("RECEIVED")) {
            usageConsumption.setStatus("VALIDATED");
        } else if (oldState.equals("VALIDATED")) {
            usageConsumption.setStatus("BILLED");
        } else if (oldState.equals("BILLED")) {
            // Terminal state
        } else {
            usageConsumption.setStatus("REJECTED");
        }
        
        if (oldState != null && !oldState.equals(usageConsumption.getStatus())) {
            eventPublisher.publishEvent(new UsageConsumptionStateChangeEvent(usageConsumption, oldState, usageConsumption.getStatus()));
        }
    }
}
