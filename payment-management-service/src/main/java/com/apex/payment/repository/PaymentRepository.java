package com.apex.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.apex.payment.entity.Payment;

public interface PaymentRepository
        extends JpaRepository<Payment, Long> {

}