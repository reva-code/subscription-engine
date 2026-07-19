package com.apex.payment.gateway.gateway;

import com.apex.payment.gateway.dto.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component("mockPaymentGateway")
public class MockPaymentGateway implements PaymentGateway {

    @Override
    public PaymentResponse authorize(PaymentRequest request) {
        String transactionId = "mock_auth_" + UUID.randomUUID().toString().replace("-", "").substring(0, 12);
        
        // Simulating failure if payment method contains word "fail"
        boolean shouldFail = request.getPaymentMethod() != null && request.getPaymentMethod().toLowerCase().contains("fail");
        GatewayStatus status = shouldFail ? GatewayStatus.FAILED : GatewayStatus.AUTHORIZED;
        String failureReason = shouldFail ? "PAYMENT_DECLINED" : null;

        Map<String, Object> rawPayload = new HashMap<>();
        rawPayload.put("mock_provider", "mock");
        rawPayload.put("mock_authorized", !shouldFail);

        return PaymentResponse.builder()
                .transactionId(transactionId)
                .status(status)
                .amount(request.getAmount())
                .currency(request.getCurrency())
                .failureReason(failureReason)
                .rawPayload(rawPayload)
                .build();
    }

    @Override
    public PaymentResponse capture(String transactionId) {
        boolean shouldFail = transactionId != null && transactionId.contains("fail");
        GatewayStatus status = shouldFail ? GatewayStatus.FAILED : GatewayStatus.CAPTURED;
        String failureReason = shouldFail ? "PAYMENT_DECLINED" : null;

        Map<String, Object> rawPayload = new HashMap<>();
        rawPayload.put("mock_provider", "mock");
        rawPayload.put("mock_captured", !shouldFail);

        return PaymentResponse.builder()
                .transactionId(transactionId)
                .status(status)
                .amount(new BigDecimal("100.00")) // standard mock value
                .currency("INR")
                .failureReason(failureReason)
                .rawPayload(rawPayload)
                .build();
    }

    @Override
    public RefundResponse refund(String transactionId, BigDecimal amount) {
        String refundId = "mock_ref_" + UUID.randomUUID().toString().replace("-", "").substring(0, 12);
        boolean shouldFail = transactionId != null && transactionId.contains("fail");
        GatewayStatus status = shouldFail ? GatewayStatus.FAILED : GatewayStatus.REFUNDED;
        String failureReason = shouldFail ? "PAYMENT_DECLINED" : null;

        Map<String, Object> rawPayload = new HashMap<>();
        rawPayload.put("mock_provider", "mock");
        rawPayload.put("mock_refunded", !shouldFail);

        return RefundResponse.builder()
                .refundId(refundId)
                .originalTransactionId(transactionId)
                .amount(amount)
                .currency("INR")
                .status(status)
                .failureReason(failureReason)
                .rawPayload(rawPayload)
                .build();
    }

    @Override
    public GatewayStatus getStatus(String transactionId) {
        if (transactionId == null) {
            return GatewayStatus.UNKNOWN;
        }
        if (transactionId.contains("fail")) {
            return GatewayStatus.FAILED;
        }
        if (transactionId.contains("auth")) {
            return GatewayStatus.AUTHORIZED;
        }
        if (transactionId.contains("ref")) {
            return GatewayStatus.REFUNDED;
        }
        return GatewayStatus.CAPTURED;
    }
}
