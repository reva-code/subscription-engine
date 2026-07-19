package com.apex.payment.gateway.exception;

public class WebhookValidationException extends PaymentGatewayException {

    public WebhookValidationException(String message, String errorCode, String failureReason) {
        super(message, errorCode, failureReason);
    }

    public WebhookValidationException(String message, String errorCode, String failureReason, Throwable cause) {
        super(message, errorCode, failureReason, cause);
    }
}
