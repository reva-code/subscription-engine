package com.jio.productinventory.client;

import com.jio.productinventory.client.dto.CommunicationMessageRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * HTTP client for TMF681 Communication Management Service.
 * Fire-and-forget — failures only log a warning, never block the caller.
 */
@Component
@Slf4j
public class CommunicationClient {

    public static final String COMMUNICATION_TYPE_SMS = "SMS";

    private final RestTemplate restTemplate;

    @Value("${app.service-urls.communication}")
    private String communicationServiceUrl;

    public CommunicationClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void sendSms(String customerId, String receiverMobile, String subject, String messageBody) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            CommunicationMessageRequestDto body = CommunicationMessageRequestDto.builder()
                    .customerId(customerId)
                    .receiverMobile(receiverMobile)
                    .communicationType(COMMUNICATION_TYPE_SMS)
                    .subject(subject)
                    .messageBody(messageBody)
                    .build();

            restTemplate.postForObject(
                    communicationServiceUrl + "/communicationMessage",
                    new HttpEntity<>(body, headers),
                    Object.class
            );
            log.info("SMS queued for customer {}", customerId);
        } catch (Exception e) {
            log.warn("Could not send SMS for customer {}: {}", customerId, e.getMessage());
        }
    }
}
