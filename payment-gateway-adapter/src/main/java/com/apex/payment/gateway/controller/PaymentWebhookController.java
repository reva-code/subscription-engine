package com.apex.payment.gateway.controller;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apex.payment.gateway.config.PaymentGatewayProperties;
import com.apex.payment.gateway.dto.GatewayWebhookEvent;
import com.apex.payment.gateway.entity.ProcessedWebhook;
import com.apex.payment.gateway.exception.WebhookValidationException;
import com.apex.payment.gateway.repository.ProcessedWebhookRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/tmf-api/paymentManagement/v4")
public class PaymentWebhookController {

    private static final Logger log = LoggerFactory.getLogger(PaymentWebhookController.class);

    private final ProcessedWebhookRepository webhookRepository;
    private final PaymentGatewayProperties properties;
    private final ObjectMapper objectMapper;

    public PaymentWebhookController(ProcessedWebhookRepository webhookRepository,
                                    PaymentGatewayProperties properties,
                                    ObjectMapper objectMapper) {
        this.webhookRepository = webhookRepository;
        this.properties = properties;
        this.objectMapper = objectMapper;
    }

    @PostMapping("/webhooks/payment")
    public ResponseEntity<Map<String, Object>> handleWebhook(
            @RequestBody String rawBody,
            @RequestHeader(value = "X-Razorpay-Signature", required = false) String signatureHeader) {

        log.info("Received payment gateway webhook event");

        boolean isMock = "mock".equalsIgnoreCase(properties.getProvider());
        if (!isMock) {
            if (!isValidSignature(rawBody, signatureHeader)) {
                log.error("Webhook signature verification failed");
                throw new WebhookValidationException("Invalid webhook signature", "ERR_INVALID_SIGNATURE", "PAYMENT_REJECTED");
            }
        }

        GatewayWebhookEvent event;
        try {
            event = objectMapper.readValue(rawBody, GatewayWebhookEvent.class);
        } catch (Exception e) {
            log.error("Failed to parse webhook payload", e);
            throw new WebhookValidationException("Malformed webhook payload", "ERR_MALFORMED_PAYLOAD", "INVALID_PAYMENT_METHOD", e);
        }

        if (event.getId() == null || event.getId().trim().isEmpty()) {
            throw new WebhookValidationException("Webhook event ID is missing", "ERR_MISSING_EVENT_ID", "INVALID_PAYMENT_METHOD");
        }
        if (event.getEvent() == null || event.getEvent().trim().isEmpty()) {
            throw new WebhookValidationException("Webhook event type is missing", "ERR_MISSING_EVENT_TYPE", "INVALID_PAYMENT_METHOD");
        }
        if (event.getPayload() == null || event.getPayload().isEmpty()) {
            throw new WebhookValidationException("Webhook payload is missing", "ERR_MISSING_PAYLOAD", "INVALID_PAYMENT_METHOD");
        }

        if (webhookRepository.existsByEventId(event.getId())) {
            log.warn("Webhook event with ID {} has already been processed. Ignoring duplicate.", event.getId());
            Map<String, Object> response = new HashMap<>();
            response.put("status", "ignored");
            response.put("message", "Duplicate event ignored");
            response.put("eventId", event.getId());
            return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body(response);
        }

        ProcessedWebhook processedWebhook = ProcessedWebhook.builder()
                .eventId(event.getId())
                .processedAt(Instant.now())
                .build();
        webhookRepository.save(processedWebhook);

        log.info("Processing webhook event: {} with ID: {}", event.getEvent(), event.getId());
        switch (event.getEvent()) {
            case "payment.authorized":
                log.info("Payment authorized webhook event processed successfully.");
                break;
            case "payment.captured":
                log.info("Payment captured webhook event processed successfully.");
                break;
            case "payment.failed":
                log.info("Payment failed webhook event processed successfully.");
                break;
            case "refund.processed":
                log.info("Refund processed webhook event processed successfully.");
                break;
            default:
                log.warn("Unhandled webhook event type: {}", event.getEvent());
        }

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Webhook processed successfully");
        response.put("eventId", event.getId());
        return ResponseEntity.ok(response);
    }

    private boolean isValidSignature(String payload, String signatureHeader) {
        if (signatureHeader == null || signatureHeader.trim().isEmpty()) {
            return false;
        }

        String secret = properties.getRazorpay().getWebhookSecret();
        if (properties.getProvider() == null || "razorpay".equalsIgnoreCase(properties.getProvider())) {
            if (secret == null || secret.isBlank()) {
                log.error("Razorpay webhook secret is not configured while provider is razorpay.");
                return false;
            }
        }

        try {
            Mac sha256HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            sha256HMAC.init(secretKey);
            byte[] computed = sha256HMAC.doFinal(payload.getBytes(StandardCharsets.UTF_8));
            byte[] received = hexStringToByteArray(signatureHeader);
            return MessageDigest.isEqual(computed, received);
        } catch (Exception e) {
            log.error("Failed to verify webhook signature due to error", e);
            return false;
        }
    }

    private byte[] hexStringToByteArray(String hex) {
        int len = hex.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                    + Character.digit(hex.charAt(i + 1), 16));
        }
        return data;
    }
}
