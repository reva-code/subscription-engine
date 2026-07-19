package com.apex.payment.gateway.gateway;

import com.apex.payment.gateway.dto.PaymentRequest;
import com.apex.payment.gateway.dto.PaymentResponse;
import com.apex.payment.gateway.dto.RefundResponse;
import com.apex.payment.gateway.dto.GatewayStatus;

import java.math.BigDecimal;

public interface PaymentGateway {
    PaymentResponse authorize(PaymentRequest request);
    PaymentResponse capture(String transactionId);
    RefundResponse refund(String transactionId, BigDecimal amount);
    GatewayStatus getStatus(String transactionId);
}
