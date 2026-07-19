package com.apex.payment.gateway.dto;

import java.math.BigDecimal;
import java.util.Map;

public class PaymentRequest {
    private BigDecimal amount;
    private String currency;
    private String paymentMethod;
    private String description;
    private String customerEmail;
    private String customerPhone;
    private Map<String, Object> metadata;

    public PaymentRequest() {}

    public PaymentRequest(BigDecimal amount, String currency, String paymentMethod, String description,
                          String customerEmail, String customerPhone, Map<String, Object> metadata) {
        this.amount = amount;
        this.currency = currency;
        this.paymentMethod = paymentMethod;
        this.description = description;
        this.customerEmail = customerEmail;
        this.customerPhone = customerPhone;
        this.metadata = metadata;
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

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private BigDecimal amount;
        private String currency;
        private String paymentMethod;
        private String description;
        private String customerEmail;
        private String customerPhone;
        private Map<String, Object> metadata;

        public Builder amount(BigDecimal amount) {
            this.amount = amount;
            return this;
        }

        public Builder currency(String currency) {
            this.currency = currency;
            return this;
        }

        public Builder paymentMethod(String paymentMethod) {
            this.paymentMethod = paymentMethod;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder customerEmail(String customerEmail) {
            this.customerEmail = customerEmail;
            return this;
        }

        public Builder customerPhone(String customerPhone) {
            this.customerPhone = customerPhone;
            return this;
        }

        public Builder metadata(Map<String, Object> metadata) {
            this.metadata = metadata;
            return this;
        }

        public PaymentRequest build() {
            return new PaymentRequest(amount, currency, paymentMethod, description, customerEmail, customerPhone, metadata);
        }
    }
}
