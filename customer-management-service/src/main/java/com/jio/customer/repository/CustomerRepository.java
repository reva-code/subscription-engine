package com.jio.customer.repository;

import com.jio.customer.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {

    List<Customer> findByStatus(String status);

    /** Look up a customer by their party id (cross-service join) */
    Optional<Customer> findByEngagedPartyId(String engagedPartyId);

    List<Customer> findByCustomerRank(String customerRank);
}
