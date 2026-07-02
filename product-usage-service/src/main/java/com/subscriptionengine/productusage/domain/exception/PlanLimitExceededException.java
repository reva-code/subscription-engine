package com.subscriptionengine.productusage.domain.exception;

public class PlanLimitExceededException extends UsageValidationException {
    public PlanLimitExceededException(String message) {
        super(message);
    }
}
