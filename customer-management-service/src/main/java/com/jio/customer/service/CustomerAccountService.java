package com.jio.customer.service;

import com.jio.customer.model.Customer;
import com.jio.customer.model.CustomerAccount;
import com.jio.customer.repository.CustomerAccountRepository;
import com.jio.customer.repository.CustomerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CustomerAccountService {

    private final CustomerAccountRepository accountRepository;
    private final CustomerRepository        customerRepository;

    public CustomerAccountService(CustomerAccountRepository accountRepository,
                                  CustomerRepository customerRepository) {
        this.accountRepository  = accountRepository;
        this.customerRepository = customerRepository;
    }

    /**
     * Create an account. The request body must include customer.id so we
     * can resolve the FK. Throws 404 if the customer doesn't exist.
     */
    public CustomerAccount create(CustomerAccount account) {
        // Resolve the Customer FK from the nested customer.id
        if (account.getCustomer() == null || account.getCustomer().getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "customer.id is required to create a CustomerAccount");
        }
        Customer customer = customerRepository.findById(account.getCustomer().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Customer not found: " + account.getCustomer().getId()));

        account.setCustomer(customer);
        account.setCreatedAt(OffsetDateTime.now());
        if (account.getStatus() == null || account.getStatus().isBlank()) {
            account.setStatus("active");
        }
        if (account.getCurrency() == null || account.getCurrency().isBlank()) {
            account.setCurrency("INR");
        }
        return accountRepository.save(account);
    }

    @Transactional(readOnly = true)
    public List<CustomerAccount> findAll() {
        return accountRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<CustomerAccount> findById(String id) {
        return accountRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<CustomerAccount> findByCustomerId(String customerId) {
        return accountRepository.findByCustomerId(customerId);
    }

    public Optional<CustomerAccount> patch(String id, CustomerAccount patch) {
        return accountRepository.findById(id).map(existing -> {
            if (patch.getName()          != null) existing.setName(patch.getName());
            if (patch.getAccountType()   != null) existing.setAccountType(patch.getAccountType());
            if (patch.getStatus()        != null) existing.setStatus(patch.getStatus());
            if (patch.getCreditLimit()   != null) existing.setCreditLimit(patch.getCreditLimit());
            if (patch.getBillDay()       != null) existing.setBillDay(patch.getBillDay());
            if (patch.getPaymentStatus() != null) existing.setPaymentStatus(patch.getPaymentStatus());
            existing.setUpdatedAt(OffsetDateTime.now());
            return accountRepository.save(existing);
        });
    }

    public boolean delete(String id) {
        if (!accountRepository.existsById(id)) return false;
        accountRepository.deleteById(id);
        return true;
    }
}
