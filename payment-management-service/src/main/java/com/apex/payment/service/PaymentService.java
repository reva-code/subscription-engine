package com.apex.payment.service;

import org.springframework.stereotype.Service;

import com.apex.payment.client.CommunicationClient;
import com.apex.payment.dto.PaymentCreateRequest;
import com.apex.payment.dto.PaymentResponse;
import com.apex.payment.entity.Payment;
import com.apex.payment.entity.PaymentStatus;
import com.apex.payment.gateway.PaymentGateway;
import com.apex.payment.repository.PaymentRepository;

@Service
public class PaymentService{
private final PaymentRepository paymentRepository;

private final CommunicationClient communicationClient;

private final PaymentGateway paymentGateway;

public PaymentService(
        PaymentRepository paymentRepository,
        CommunicationClient communicationClient,
        PaymentGateway paymentGateway) {

    this.paymentRepository = paymentRepository;
    this.communicationClient = communicationClient;
    this.paymentGateway = paymentGateway;
}

public PaymentResponse createPayment(
        PaymentCreateRequest request) {

    Payment payment = new Payment();

    payment.setAmount(request.getAmount());
    payment.setCurrency(request.getCurrency());
    payment.setCustomerId(request.getCustomerId());
    payment.setOrderId(request.getOrderId());
    payment.setStatus(PaymentStatus.PENDING);

    payment = paymentRepository.save(payment);

    String gatewayResponse =
            paymentGateway.processPayment(
                    String.valueOf(payment.getId()),
                    payment.getAmount().toString());

    communicationClient.sendPaymentSuccess(
            String.valueOf(payment.getId()));

    PaymentResponse response =
            new PaymentResponse();

    response.setId(String.valueOf(payment.getId()));
    response.setAmount(request.getAmount());
    response.setCurrency(request.getCurrency());
    response.setStatus("PENDING");

    return response;
}

public PaymentResponse getPayment(
        String paymentId) {

    Payment payment =
            paymentRepository.findById(
                    Long.valueOf(paymentId))
            .orElseThrow(() ->
                    new RuntimeException(
                            "Payment not found"));

    PaymentResponse response =
            new PaymentResponse();

    response.setId(
            String.valueOf(payment.getId()));

    response.setAmount(
            payment.getAmount(  ));

    response.setCurrency(
            payment.getCurrency());

    response.setStatus(
            payment.getStatus().name());

    return response;
}

}