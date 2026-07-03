package com.apex.payment.dto;

import java.math.BigDecimal;

public class PaymentCreateRequest {

    private BigDecimal amount;

    private String currency;

    private String customerId;

    private String orderId;

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getOrderId() {
        return orderId;
    }
}