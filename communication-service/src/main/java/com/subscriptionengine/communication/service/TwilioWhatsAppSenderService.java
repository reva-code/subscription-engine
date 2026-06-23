package com.subscriptionengine.communication.service;

import com.subscriptionengine.communication.config.TwilioProperties;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TwilioWhatsAppSenderService {

    private static final Logger log = LoggerFactory.getLogger(TwilioWhatsAppSenderService.class);

    private final TwilioProperties properties;

    public TwilioWhatsAppSenderService(TwilioProperties properties) {
        this.properties = properties;
    }

    public String sendWhatsApp(String toMobileNumber, String messageBody) {
        log.info("Sending Twilio WhatsApp message to {}", toMobileNumber);

        // Validate format: starts with + followed by digits
        if (toMobileNumber == null || !toMobileNumber.matches("^\\+[1-9]\\d{1,14}$")) {
            throw new IllegalArgumentException("Invalid phone number format: " + toMobileNumber + ". Must start with '+' followed by country code and digits (E.164 format).");
        }

        String fromWhatsApp = "whatsapp:" + properties.getWhatsappNumber();
        String toWhatsApp = "whatsapp:" + toMobileNumber;

        try {
            Message message = Message.creator(
                    new PhoneNumber(toWhatsApp),
                    new PhoneNumber(fromWhatsApp),
                    messageBody
            ).create();

            log.info("Twilio WhatsApp message sent successfully. Message SID: {}", message.getSid());
            return message.getSid();
        } catch (Exception e) {
            log.error("Failed to send Twilio WhatsApp message to {}. Error: {}", toMobileNumber, e.getMessage(), e);
            throw new RuntimeException("Twilio WhatsApp dispatch failed: " + e.getMessage(), e);
        }
    }
}
