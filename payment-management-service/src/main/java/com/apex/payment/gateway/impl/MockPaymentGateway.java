package com.apex.payment.gateway.impl;

import org.springframework.stereotype.Component;

import com.apex.payment.gateway.PaymentGateway;

@Component
public class MockPaymentGateway
        implements PaymentGateway {

    @Override
    public String processPayment(
            String paymentId,
            String amount) {

        System.out.println(
                "Processing payment via Mock Gateway: "
                        + paymentId);

        return "SUCCESS";
    }
}