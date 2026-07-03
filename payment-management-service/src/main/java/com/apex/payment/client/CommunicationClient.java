package com.apex.payment.client;

public interface CommunicationClient {

    void sendPaymentSuccess(
            String paymentId);

    void sendPaymentFailure(
            String paymentId);
}