package com.apex.payment.client.impl;

import org.springframework.stereotype.Component;

import com.apex.payment.client.CommunicationClient;

@Component
public class CommunicationClientImpl
        implements CommunicationClient {

    @Override
    public void sendPaymentSuccess(
            String paymentId) {

        System.out.println(
                "TMF681 SUCCESS notification for payment: "
                        + paymentId);
    }

    @Override
    public void sendPaymentFailure(
            String paymentId) {

        System.out.println(
                "TMF681 FAILURE notification for payment: "
                        + paymentId);
    }
}