package com.subscriptionengine.communication.adapter;

public interface SmsSenderAdapter {
    void sendSms(String phoneNumber, String message);
}
