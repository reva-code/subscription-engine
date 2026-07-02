package com.subscriptionengine.productusage.domain.exception;

public class ActiveSubscriptionValidationException extends UsageValidationException {
    public ActiveSubscriptionValidationException(String message) {
        super(message);
    }
}
