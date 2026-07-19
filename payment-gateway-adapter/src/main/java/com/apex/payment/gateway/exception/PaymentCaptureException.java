package com.apex.payment.gateway.exception;

public class PaymentCaptureException extends PaymentGatewayException {

    public PaymentCaptureException(String message, String errorCode, String failureReason) {
        super(message, errorCode, failureReason);
    }

    public PaymentCaptureException(String message, String errorCode, String failureReason, Throwable cause) {
        super(message, errorCode, failureReason, cause);
    }
}
