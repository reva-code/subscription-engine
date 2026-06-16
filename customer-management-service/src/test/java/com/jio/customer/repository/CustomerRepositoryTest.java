package com.jio.customer.repository;

import com.jio.customer.model.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomerRepositoryTest {

    @Container
    static MariaDBContainer<?> mariaDB = new MariaDBContainer<>("mariadb:11")
            .withDatabaseName("jio_customer_test")
            .withUsername("root")
            .withPassword("test");

    @DynamicPropertySource
    static void overrideDataSource(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url",      mariaDB::getJdbcUrl);
        registry.add("spring.datasource.username", mariaDB::getUsername);
        registry.add("spring.datasource.password", mariaDB::getPassword);
        registry.add("spring.datasource.driver-class-name", () -> "org.mariadb.jdbc.Driver");
    }

    @Autowired
    CustomerRepository repository;

    @BeforeEach
    void cleanUp() {
        repository.deleteAll();
    }

    // ── save & findById ───────────────────────────────────────────────────────

    @Test
    void save_assignsUuid_andFindByIdReturnsIt() {
        Customer c = new Customer("Saanvi Sharma", "party-001");
        Customer saved = repository.save(c);

        assertThat(saved.getId()).isNotBlank();

        Optional<Customer> found = repository.findById(saved.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Saanvi Sharma");
    }

    // ── findByEngagedPartyId ──────────────────────────────────────────────────

    @Test
    void findByEngagedPartyId_match_returnsCustomer() {
        repository.save(new Customer("Alice", "party-aaa"));
        repository.save(new Customer("Bob",   "party-bbb"));

        Optional<Customer> result = repository.findByEngagedPartyId("party-aaa");

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Alice");
    }

    @Test
    void findByEngagedPartyId_noMatch_returnsEmpty() {
        assertThat(repository.findByEngagedPartyId("party-xyz")).isEmpty();
    }

    // ── findByStatus ──────────────────────────────────────────────────────────

    @Test
    void findByStatus_returnsOnlyMatchingRecords() {
        Customer active = new Customer("Alice", "p-1"); active.setStatus("active");
        Customer inact  = new Customer("Bob",   "p-2"); inact.setStatus("inactive");
        repository.saveAll(List.of(active, inact));

        List<Customer> result = repository.findByStatus("active");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Alice");
    }

    // ── findByCustomerRank ────────────────────────────────────────────────────

    @Test
    void findByCustomerRank_returnsOnlyMatchingRecords() {
        Customer gold     = new Customer("Neha",   "p-1"); gold.setCustomerRank("Gold");
        Customer platinum = new Customer("Chitra", "p-2"); platinum.setCustomerRank("Platinum");
        repository.saveAll(List.of(gold, platinum));

        List<Customer> result = repository.findByCustomerRank("Gold");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Neha");
    }

    // ── deleteById ────────────────────────────────────────────────────────────

    @Test
    void deleteById_removesRecord() {
        Customer saved = repository.save(new Customer("Akshat", "p-3"));
        String id = saved.getId();

        repository.deleteById(id);

        assertThat(repository.findById(id)).isEmpty();
    }

    // ── findAll ───────────────────────────────────────────────────────────────

    @Test
    void findAll_returnsAllSavedRecords() {
        repository.saveAll(List.of(
                new Customer("Alice", "p-1"),
                new Customer("Bob",   "p-2"),
                new Customer("Carol", "p-3")
        ));

        assertThat(repository.findAll()).hasSize(3);
    }
}
