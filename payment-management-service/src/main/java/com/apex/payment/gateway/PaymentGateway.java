package com.apex.payment.gateway;

public interface PaymentGateway {

    String processPayment(
            String paymentId,
            String amount);
}