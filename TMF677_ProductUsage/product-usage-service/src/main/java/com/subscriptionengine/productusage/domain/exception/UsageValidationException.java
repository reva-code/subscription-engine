package com.subscriptionengine.productusage.domain.exception;

public class UsageValidationException extends RuntimeException {
    public UsageValidationException(String message) {
        super(message);
    }
}
