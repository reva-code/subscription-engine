package com.apex.payment.gateway.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.apex.payment.gateway.config.PaymentGatewayProperties;
import com.apex.payment.gateway.repository.ProcessedWebhookRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = PaymentWebhookController.class)
public class PaymentWebhookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProcessedWebhookRepository webhookRepository;

    @MockBean
    private PaymentGatewayProperties properties;

    private PaymentGatewayProperties.RazorpayProperties razorpayProperties;

    @BeforeEach
    public void setup() {
        razorpayProperties = new PaymentGatewayProperties.RazorpayProperties();
        razorpayProperties.setWebhookSecret("testsecret");
        when(properties.getProvider()).thenReturn("razorpay");
        when(properties.getRazorpay()).thenReturn(razorpayProperties);
        when(webhookRepository.existsByEventId(any())).thenReturn(false);
    }

    @Test
    public void validWebhookReturnsOk() throws Exception {
        Map<String, Object> event = new HashMap<>();
        event.put("id", "evt_test_1");
        event.put("entity", "event");
        event.put("event", "payment.captured");
        event.put("contains", List.of("payment"));
        event.put("payload", Map.of("payment", Map.of("entity", Map.of("id", "pay_123"))));
        event.put("createdAt", 1650000000L);

        String payload = objectMapper.writeValueAsString(event);
        String signature = calculateHmacSha256(payload, "testsecret");

        mockMvc.perform(post("/tmf-api/paymentManagement/v4/webhooks/payment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Razorpay-Signature", signature)
                        .content(payload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.eventId").value("evt_test_1"));
    }

    @Test
    public void invalidSignatureReturnsBadRequest() throws Exception {
        Map<String, Object> event = new HashMap<>();
        event.put("id", "evt_test_2");
        event.put("entity", "event");
        event.put("event", "payment.failed");
        event.put("contains", List.of("payment"));
        event.put("payload", Map.of("payment", Map.of("entity", Map.of("id", "pay_123"))));
        event.put("createdAt", 1650000000L);

        String payload = objectMapper.writeValueAsString(event);

        mockMvc.perform(post("/tmf-api/paymentManagement/v4/webhooks/payment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Razorpay-Signature", "invalidsignature")
                        .content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("ERR_INVALID_SIGNATURE"));
    }

    @Test
    public void duplicateWebhookReturnsAlreadyReported() throws Exception {
        when(webhookRepository.existsByEventId("evt_test_3")).thenReturn(true);

        Map<String, Object> event = new HashMap<>();
        event.put("id", "evt_test_3");
        event.put("entity", "event");
        event.put("event", "payment.captured");
        event.put("contains", List.of("payment"));
        event.put("payload", Map.of("payment", Map.of("entity", Map.of("id", "pay_123"))));
        event.put("createdAt", 1650000000L);

        String payload = objectMapper.writeValueAsString(event);
        String signature = calculateHmacSha256(payload, "testsecret");

        mockMvc.perform(post("/tmf-api/paymentManagement/v4/webhooks/payment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Razorpay-Signature", signature)
                        .content(payload))
                .andExpect(status().isAlreadyReported())
                .andExpect(jsonPath("$.status").value("ignored"));
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
}
