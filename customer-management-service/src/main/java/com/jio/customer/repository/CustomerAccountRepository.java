package com.jio.customer.repository;

import com.jio.customer.model.CustomerAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerAccountRepository extends JpaRepository<CustomerAccount, String> {

    /** Get all accounts for a given customer */
    List<CustomerAccount> findByCustomerId(String customerId);

    List<CustomerAccount> findByAccountType(String accountType);

    List<CustomerAccount> findByPaymentStatus(String paymentStatus);
}
