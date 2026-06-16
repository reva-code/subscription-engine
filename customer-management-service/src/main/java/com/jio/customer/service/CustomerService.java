package com.jio.customer.service;

import com.jio.customer.client.CommunicationClient;
import com.jio.customer.client.PartyClient;
import com.jio.customer.model.Customer;
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
public class CustomerService {

    private final CustomerRepository repository;
    private final PartyClient partyClient;
    private final CommunicationClient communicationClient;

    public CustomerService(CustomerRepository repository,
                           PartyClient partyClient,
                           CommunicationClient communicationClient) {
        this.repository = repository;
        this.partyClient = partyClient;
        this.communicationClient = communicationClient;
    }

    public Customer create(Customer customer) {
        // Wire 1: validate that the referenced Individual exists in TMF632 Party Management
        if (customer.getEngagedPartyId() != null && !customer.getEngagedPartyId().isBlank()) {
            if (!partyClient.individualExists(customer.getEngagedPartyId())) {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                    "engagedPartyId '" + customer.getEngagedPartyId() +
                    "' does not exist in Party Management (TMF632). " +
                    "Create the Individual first at POST /tmf-api/partyManagement/v4/individual");
            }
        }
        customer.setCreatedAt(OffsetDateTime.now());
        if (customer.getStatus() == null || customer.getStatus().isBlank()) {
            customer.setStatus("prospect");
        }
        Customer saved = repository.save(customer);

        // Wire 15: fire welcome notification to TMF681 (fire-and-forget)
        communicationClient.sendWelcome(saved.getId(), saved.getName());

        return saved;
    }

    @Transactional(readOnly = true)
    public List<Customer> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Customer> findById(String id) {
        return repository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Customer> findByEngagedPartyId(String engagedPartyId) {
        return repository.findByEngagedPartyId(engagedPartyId);
    }

    public Optional<Customer> patch(String id, Customer patch) {
        return repository.findById(id).map(existing -> {
            if (patch.getName()             != null) existing.setName(patch.getName());
            if (patch.getStatus()           != null) existing.setStatus(patch.getStatus());
            if (patch.getStatusReason()     != null) existing.setStatusReason(patch.getStatusReason());
            if (patch.getCustomerRank()     != null) existing.setCustomerRank(patch.getCustomerRank());
            if (patch.getEngagedPartyId()   != null) existing.setEngagedPartyId(patch.getEngagedPartyId());
            if (patch.getEngagedPartyHref() != null) existing.setEngagedPartyHref(patch.getEngagedPartyHref());
            if (patch.getEngagedPartyName() != null) existing.setEngagedPartyName(patch.getEngagedPartyName());
            if (!patch.getCharacteristic().isEmpty()) existing.setCharacteristic(patch.getCharacteristic());
            existing.setUpdatedAt(OffsetDateTime.now());
            return repository.save(existing);
        });
    }

    public boolean delete(String id) {
        if (!repository.existsById(id)) return false;
        repository.deleteById(id);
        return true;
    }
}
