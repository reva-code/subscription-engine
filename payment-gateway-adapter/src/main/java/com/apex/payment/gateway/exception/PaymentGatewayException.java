package com.apex.payment.gateway.exception;

public class PaymentGatewayException extends RuntimeException {
    private final String errorCode;
    private final String failureReason; // TMF676 failure reason code

    public PaymentGatewayException(String message, String errorCode, String failureReason) {
        super(message);
        this.errorCode = errorCode;
        this.failureReason = failureReason;
    }

    public PaymentGatewayException(String message, String errorCode, String failureReason, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.failureReason = failureReason;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getFailureReason() {
        return failureReason;
    }
}
