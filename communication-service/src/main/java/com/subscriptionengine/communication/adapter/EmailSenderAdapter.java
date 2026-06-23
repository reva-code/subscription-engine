package com.subscriptionengine.communication.adapter;

public interface EmailSenderAdapter {
    void sendEmail(String to, String subject, String body);
}
