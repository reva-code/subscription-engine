package com.apex.payment.gateway.dto;

import java.math.BigDecimal;
import java.util.Map;

public class PaymentResponse {
    private String transactionId;
    private GatewayStatus status;
    private BigDecimal amount;
    private String currency;
    private String failureReason; // TMF676-aligned reason code
    private String gatewayErrorCode;
    private String gatewayErrorMessage;
    private Map<String, Object> rawPayload;

    public PaymentResponse() {}

    public PaymentResponse(String transactionId, GatewayStatus status, BigDecimal amount, String currency,
                           String failureReason, String gatewayErrorCode, String gatewayErrorMessage,
                           Map<String, Object> rawPayload) {
        this.transactionId = transactionId;
        this.status = status;
        this.amount = amount;
        this.currency = currency;
        this.failureReason = failureReason;
        this.gatewayErrorCode = gatewayErrorCode;
        this.gatewayErrorMessage = gatewayErrorMessage;
        this.rawPayload = rawPayload;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public GatewayStatus getStatus() {
        return status;
    }

    public void setStatus(GatewayStatus status) {
        this.status = status;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }

    public String getGatewayErrorCode() {
        return gatewayErrorCode;
    }

    public void setGatewayErrorCode(String gatewayErrorCode) {
        this.gatewayErrorCode = gatewayErrorCode;
    }

    public String getGatewayErrorMessage() {
        return gatewayErrorMessage;
    }

    public void setGatewayErrorMessage(String gatewayErrorMessage) {
        this.gatewayErrorMessage = gatewayErrorMessage;
    }

    public Map<String, Object> getRawPayload() {
        return rawPayload;
    }

    public void setRawPayload(Map<String, Object> rawPayload) {
        this.rawPayload = rawPayload;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String transactionId;
        private GatewayStatus status;
        private BigDecimal amount;
        private String currency;
        private String failureReason;
        private String gatewayErrorCode;
        private String gatewayErrorMessage;
        private Map<String, Object> rawPayload;

        public Builder transactionId(String transactionId) {
            this.transactionId = transactionId;
            return this;
        }

        public Builder status(GatewayStatus status) {
            this.status = status;
            return this;
        }

        public Builder amount(BigDecimal amount) {
            this.amount = amount;
            return this;
        }

        public Builder currency(String currency) {
            this.currency = currency;
            return this;
        }

        public Builder failureReason(String failureReason) {
            this.failureReason = failureReason;
            return this;
        }

        public Builder gatewayErrorCode(String gatewayErrorCode) {
            this.gatewayErrorCode = gatewayErrorCode;
            return this;
        }

        public Builder gatewayErrorMessage(String gatewayErrorMessage) {
            this.gatewayErrorMessage = gatewayErrorMessage;
            return this;
        }

        public Builder rawPayload(Map<String, Object> rawPayload) {
            this.rawPayload = rawPayload;
            return this;
        }

        public PaymentResponse build() {
            return new PaymentResponse(transactionId, status, amount, currency, failureReason, gatewayErrorCode, gatewayErrorMessage, rawPayload);
        }
    }
}
