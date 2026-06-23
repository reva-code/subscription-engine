package com.subscriptionengine.communication.service;

import com.subscriptionengine.communication.config.TwilioProperties;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TwilioSmsSenderService {

    private static final Logger log = LoggerFactory.getLogger(TwilioSmsSenderService.class);

    private final TwilioProperties properties;

    public TwilioSmsSenderService(TwilioProperties properties) {
        this.properties = properties;
    }

    @PostConstruct
    public void init() {
        if (properties.getAccountSid() != null && !properties.getAccountSid().trim().isEmpty() &&
            properties.getAuthToken() != null && !properties.getAuthToken().trim().isEmpty()) {
            Twilio.init(properties.getAccountSid(), properties.getAuthToken());
            log.info("Twilio SDK initialized successfully");
        } else {
            log.warn("Twilio credentials not fully set. SMS and WhatsApp features will fail during live sends.");
        }
    }

    public String sendSms(String toMobileNumber, String messageBody) {
        log.info("Sending Twilio SMS to {}", toMobileNumber);

        // Validate format: starts with + followed by digits
        if (toMobileNumber == null || !toMobileNumber.matches("^\\+[1-9]\\d{1,14}$")) {
            throw new IllegalArgumentException("Invalid phone number format: " + toMobileNumber + ". Must start with '+' followed by country code and digits (E.164 format).");
        }

        try {
            Message message = Message.creator(
                    new PhoneNumber(toMobileNumber),
                    new PhoneNumber(properties.getPhoneNumber()),
                    messageBody
            ).create();

            log.info("Twilio SMS sent successfully. Message SID: {}", message.getSid());
            return message.getSid();
        } catch (Exception e) {
            log.error("Failed to send Twilio SMS to {}. Error: {}", toMobileNumber, e.getMessage(), e);
            throw new RuntimeException("Twilio SMS dispatch failed: " + e.getMessage(), e);
        }
    }
}
