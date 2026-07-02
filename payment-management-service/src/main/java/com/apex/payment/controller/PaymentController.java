package com.apex.payment.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.apex.payment.dto.PaymentCreateRequest;
import com.apex.payment.dto.PaymentResponse;
import com.apex.payment.service.PaymentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;

@RestController
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(
            PaymentService paymentService) {

        this.paymentService = paymentService;
    }

    @PostMapping("/payment")
    public PaymentResponse createPayment(
            @RequestBody PaymentCreateRequest request) {

        return paymentService.createPayment(request);

    }
   @GetMapping("/payment/{id}")
public PaymentResponse getPayment(
        @PathVariable("id") String id) {

    return paymentService.getPayment(id);
}

}
