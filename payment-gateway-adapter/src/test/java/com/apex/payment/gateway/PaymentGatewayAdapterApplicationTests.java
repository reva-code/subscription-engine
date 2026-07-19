package com.apex.payment.gateway;

import com.apex.payment.gateway.config.PaymentGatewayProperties;
import com.apex.payment.gateway.dto.*;
import com.apex.payment.gateway.entity.ProcessedWebhook;
import com.apex.payment.gateway.gateway.PaymentGateway;
import com.apex.payment.gateway.mapper.GatewayErrorMapper;
import com.apex.payment.gateway.repository.ProcessedWebhookRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PaymentGatewayAdapterApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PaymentGateway activePaymentGateway;

    @Autowired
    private PaymentGatewayProperties properties;

    @Autowired
    private GatewayErrorMapper errorMapper;

    @Autowired
    private ProcessedWebhookRepository webhookRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        webhookRepository.deleteAll();
    }

    @Test
    public void contextLoads() {
        assertNotNull(activePaymentGateway);
        assertNotNull(properties);
        assertNotNull(errorMapper);
        assertEquals("razorpay", properties.getProvider());
    }

    @Test
    public void testErrorMapping() {
        assertEquals("PAYMENT_DECLINED", errorMapper.mapToTmfReason("payment_failed"));
        assertEquals("INSUFFICIENT_FUNDS", errorMapper.mapToTmfReason("insufficient_funds"));
        assertEquals("INVALID_PAYMENT_METHOD", errorMapper.mapToTmfReason("invalid_card"));
        assertEquals("PAYMENT_TIMEOUT", errorMapper.mapToTmfReason("gateway_timeout"));
        assertEquals("PAYMENT_REJECTED", errorMapper.mapToTmfReason("fraud_blocked"));
        
        assertEquals("PAYMENT_DECLINED", errorMapper.mapToTmfReason("PAYMENT-FAILED"));
        assertEquals("INSUFFICIENT_FUNDS", errorMapper.mapToTmfReason("INSUFFICIENT FUNDS"));
        assertEquals("PAYMENT_DECLINED", errorMapper.mapToTmfReason("unknown_error_code"));
    }

    @Test
    public void testWebhookProcessingAndIdempotency() throws Exception {
        String eventId = "evt_test_" + System.currentTimeMillis();
        Map<String, Object> innerPayload = new HashMap<>();
        Map<String, Object> paymentPayload = new HashMap<>();
        Map<String, Object> entityPayload = new HashMap<>();
        entityPayload.put("id", "pay_mock123");
        entityPayload.put("amount", 50000);
        entityPayload.put("status", "captured");
        paymentPayload.put("entity", entityPayload);
        innerPayload.put("payment", paymentPayload);

        GatewayWebhookEvent webhookEvent = GatewayWebhookEvent.builder()
                .id(eventId)
                .entity("event")
                .event("payment.captured")
                .contains(List.of("payment"))
                .payload(innerPayload)
                .createdAt(1600000000L)
                .build();

        String payloadJson = objectMapper.writeValueAsString(webhookEvent);
        String secret = properties.getRazorpay().getWebhookSecret();
        String signature = calculateHmacSha256(payloadJson, secret);

        // First delivery: Expect 200 OK
        mockMvc.perform(post("/tmf-api/paymentManagement/v4/webhooks/payment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Razorpay-Signature", signature)
                        .content(payloadJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.eventId").value(eventId));

        assertTrue(webhookRepository.existsByEventId(eventId));

        // Second delivery (Duplicate): Expect 208 Already Reported
        mockMvc.perform(post("/tmf-api/paymentManagement/v4/webhooks/payment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Razorpay-Signature", signature)
                        .content(payloadJson))
                .andExpect(status().isAlreadyReported())
                .andExpect(jsonPath("$.status").value("ignored"))
                .andExpect(jsonPath("$.message").value("Duplicate event ignored"));
    }

    private String calculateHmacSha256(String payload, String secret) throws Exception {
        javax.crypto.Mac sha256HMAC = javax.crypto.Mac.getInstance("HmacSHA256");
        javax.crypto.spec.SecretKeySpec secretKey = new javax.crypto.spec.SecretKeySpec(secret.getBytes(java.nio.charset.StandardCharsets.UTF_8), "HmacSHA256");
        sha256HMAC.init(secretKey);
        byte[] hash = sha256HMAC.doFinal(payload.getBytes(java.nio.charset.StandardCharsets.UTF_8));
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }


    @Test
    public void testMockGatewayAuthorizeSuccess() {
        com.apex.payment.gateway.gateway.MockPaymentGateway mockGateway = new com.apex.payment.gateway.gateway.MockPaymentGateway();
        
        PaymentRequest request = PaymentRequest.builder()
                .amount(new BigDecimal("150.00"))
                .currency("INR")
                .paymentMethod("card")
                .build();

        PaymentResponse response = mockGateway.authorize(request);
        assertNotNull(response.getTransactionId());
        assertEquals(GatewayStatus.AUTHORIZED, response.getStatus());
        assertEquals(new BigDecimal("150.00"), response.getAmount());
        assertNull(response.getFailureReason());
    }

    @Test
    public void testMockGatewayAuthorizeFailure() {
        com.apex.payment.gateway.gateway.MockPaymentGateway mockGateway = new com.apex.payment.gateway.gateway.MockPaymentGateway();
        
        PaymentRequest request = PaymentRequest.builder()
                .amount(new BigDecimal("150.00"))
                .currency("INR")
                .paymentMethod("card_fail")
                .build();

        PaymentResponse response = mockGateway.authorize(request);
        assertEquals(GatewayStatus.FAILED, response.getStatus());
        assertEquals("PAYMENT_DECLINED", response.getFailureReason());
    }

    @Test
    public void testMockGatewayCapture() {
        com.apex.payment.gateway.gateway.MockPaymentGateway mockGateway = new com.apex.payment.gateway.gateway.MockPaymentGateway();
        
        PaymentResponse response = mockGateway.capture("mock_auth_123");
        assertEquals(GatewayStatus.CAPTURED, response.getStatus());
        assertNull(response.getFailureReason());

        PaymentResponse failResponse = mockGateway.capture("mock_auth_fail_123");
        assertEquals(GatewayStatus.FAILED, failResponse.getStatus());
        assertEquals("PAYMENT_DECLINED", failResponse.getFailureReason());
    }

    @Test
    public void testMockGatewayRefund() {
        com.apex.payment.gateway.gateway.MockPaymentGateway mockGateway = new com.apex.payment.gateway.gateway.MockPaymentGateway();
        
        RefundResponse response = mockGateway.refund("mock_auth_123", new BigDecimal("50.00"));
        assertNotNull(response.getRefundId());
        assertEquals(GatewayStatus.REFUNDED, response.getStatus());
        assertEquals(new BigDecimal("50.00"), response.getAmount());

        RefundResponse failResponse = mockGateway.refund("mock_auth_fail_123", new BigDecimal("50.00"));
        assertEquals(GatewayStatus.FAILED, failResponse.getStatus());
    }
}
