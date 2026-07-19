package com.apex.payment.gateway.dto;

import java.math.BigDecimal;
import java.util.Map;

public class RefundResponse {
    private String refundId;
    private String originalTransactionId;
    private BigDecimal amount;
    private String currency;
    private GatewayStatus status;
    private String failureReason; // TMF676-aligned reason code
    private Map<String, Object> rawPayload;

    public RefundResponse() {}

    public RefundResponse(String refundId, String originalTransactionId, BigDecimal amount, String currency,
                          GatewayStatus status, String failureReason, Map<String, Object> rawPayload) {
        this.refundId = refundId;
        this.originalTransactionId = originalTransactionId;
        this.amount = amount;
        this.currency = currency;
        this.status = status;
        this.failureReason = failureReason;
        this.rawPayload = rawPayload;
    }

    public String getRefundId() {
        return refundId;
    }

    public void setRefundId(String refundId) {
        this.refundId = refundId;
    }

    public String getOriginalTransactionId() {
        return originalTransactionId;
    }

    public void setOriginalTransactionId(String originalTransactionId) {
        this.originalTransactionId = originalTransactionId;
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

    public GatewayStatus getStatus() {
        return status;
    }

    public void setStatus(GatewayStatus status) {
        this.status = status;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
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
        private String refundId;
        private String originalTransactionId;
        private BigDecimal amount;
        private String currency;
        private GatewayStatus status;
        private String failureReason;
        private Map<String, Object> rawPayload;

        public Builder refundId(String refundId) {
            this.refundId = refundId;
            return this;
        }

        public Builder originalTransactionId(String originalTransactionId) {
            this.originalTransactionId = originalTransactionId;
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

        public Builder status(GatewayStatus status) {
            this.status = status;
            return this;
        }

        public Builder failureReason(String failureReason) {
            this.failureReason = failureReason;
            return this;
        }

        public Builder rawPayload(Map<String, Object> rawPayload) {
            this.rawPayload = rawPayload;
            return this;
        }

        public RefundResponse build() {
            return new RefundResponse(refundId, originalTransactionId, amount, currency, status, failureReason, rawPayload);
        }
    }
}
