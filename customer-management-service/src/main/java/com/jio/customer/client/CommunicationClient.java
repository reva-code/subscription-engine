package com.jio.customer.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * HTTP client for TMF681 Communication Management Service (:8088).
 *
 * Wire 15: TMF629 → TMF681
 * Fires a welcome SMS after a new Customer is created.
 * Fire-and-forget — failure only logs a warning, never blocks the caller.
 */
@Component
public class CommunicationClient {

    private static final Logger log = LoggerFactory.getLogger(CommunicationClient.class);

    private final RestTemplate restTemplate;

    @Value("${services.tmf681.url}")
    private String communicationServiceUrl;

    public CommunicationClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Wire 15: send a welcome notification when a new customer is onboarded.
     */
    public void sendWelcome(String customerId, String customerName) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> body = new HashMap<>();
            body.put("type",               "SMS");
            body.put("subject",            "Welcome to Jio!");
            body.put("content",            "Dear " + customerName +
                                           ", welcome to Jio! Your account has been created successfully. " +
                                           "Customer ID: " + customerId);
            body.put("customerId",         customerId);
            body.put("relatedEntityType",  "Customer");
            body.put("relatedEntityId",    customerId);
            body.put("sender",             "Jio");

            restTemplate.postForObject(
                communicationServiceUrl + "/tmf-api/communicationManagement/v4/communicationMessage",
                new HttpEntity<>(body, headers),
                Object.class
            );
            log.info("[TMF629→TMF681] Welcome SMS queued for customer {}", customerId);
        } catch (Exception e) {
            log.warn("[TMF629→TMF681] Could not send welcome message for customer {}: {}", customerId, e.getMessage());
        }
    }
}
