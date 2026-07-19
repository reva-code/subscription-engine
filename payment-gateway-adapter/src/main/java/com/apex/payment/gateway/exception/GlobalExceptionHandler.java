package com.apex.payment.gateway.exception;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(WebhookValidationException.class)
    public ResponseEntity<Map<String, Object>> handleWebhookValidationException(WebhookValidationException ex) {
        return buildResponseEntity(ex.getErrorCode(), ex.getFailureReason(), ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PaymentCaptureException.class)
    public ResponseEntity<Map<String, Object>> handlePaymentCaptureException(PaymentCaptureException ex) {
        return buildResponseEntity(ex.getErrorCode(), ex.getFailureReason(), ex.getMessage(), HttpStatus.BAD_GATEWAY);
    }

    @ExceptionHandler(PaymentGatewayException.class)
    public ResponseEntity<Map<String, Object>> handlePaymentGatewayException(PaymentGatewayException ex) {
        return buildResponseEntity(ex.getErrorCode(), ex.getFailureReason(), ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(IllegalArgumentException ex) {
        return buildResponseEntity("ERR_INVALID_ARGUMENT", "INVALID_PAYMENT_METHOD", ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        return buildResponseEntity("ERR_INTERNAL_SERVER_ERROR", "PAYMENT_TIMEOUT", ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<Map<String, Object>> buildResponseEntity(String code, String reason, String message, HttpStatus status) {
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("code", code);
        errorDetails.put("reason", reason);
        errorDetails.put("message", message);
        errorDetails.put("status", status.name());
        errorDetails.put("correlationId", MDC.get("correlationId"));
        return ResponseEntity.status(status).body(errorDetails);
    }
}
