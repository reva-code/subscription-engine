package com.apex.payment.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import com.apex.payment.entity.Payment;
import com.apex.payment.entity.PaymentStatus;

public class PaymentServiceTest {

    @Test
    void testPaymentLifecycle() {

        Payment payment = new Payment();

        payment.setAmount(
                new BigDecimal("1000"));

        payment.setCurrency("INR");

        payment.setCustomerId("CUS001");

        payment.setOrderId("ORD001");

        payment.setStatus(
                PaymentStatus.PENDING);

        assertEquals(
                PaymentStatus.PENDING,
                payment.getStatus());

        assertEquals(
                "INR",
                payment.getCurrency());

        assertEquals(
                "CUS001",
                payment.getCustomerId());
    }

    @Test
    void testPendingStatus() {

        Payment payment = new Payment();

        payment.setStatus(
                PaymentStatus.PENDING);

        assertEquals(
                PaymentStatus.PENDING,
                payment.getStatus());
    }

    @Test
    void testSuccessStatus() {

        Payment payment = new Payment();

        payment.setStatus(
                PaymentStatus.SUCCESS);

        assertEquals(
                PaymentStatus.SUCCESS,
                payment.getStatus());
    }

    @Test
    void testFailedStatus() {

        Payment payment = new Payment();

        payment.setStatus(
                PaymentStatus.FAILED);

        assertEquals(
                PaymentStatus.FAILED,
                payment.getStatus());
    }
}