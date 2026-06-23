package com.subscriptionengine.communication.adapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LoggingSmsSenderAdapter implements SmsSenderAdapter {

    private static final Logger log = LoggerFactory.getLogger(LoggingSmsSenderAdapter.class);

    @Override
    public void sendSms(String phoneNumber, String message) {
        log.info("Sending SMS notification:");
        log.info("--------------------------------------------------");
        log.info("Recipient Phone: {}", phoneNumber);
        log.info("Message Content: {}", message);
        log.info("--------------------------------------------------");
        log.info("SMS message successfully logged (Adapter Pattern placeholder for Twilio/MSG91)");
    }
}
