package com.jio.customer.service;

import com.jio.customer.client.CommunicationClient;
import com.jio.customer.client.PartyClient;
import com.jio.customer.model.Customer;
import com.jio.customer.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock CustomerRepository      repository;
    @Mock PartyClient             partyClient;
    @Mock CommunicationClient     communicationClient;

    @InjectMocks CustomerService service;

    private Customer input;
    private Customer saved;

    @BeforeEach
    void setUp() {
        input = new Customer("Saanvi Sharma", "party-001");
        saved = new Customer("Saanvi Sharma", "party-001");
        saved.setId("cust-001");
        saved.setStatus("prospect");
    }

    // ── create ────────────────────────────────────────────────────────────────

    @Test
    void create_validParty_savesAndReturnsCustomer() {
        when(partyClient.individualExists("party-001")).thenReturn(true);
        when(repository.save(any(Customer.class))).thenReturn(saved);

        Customer result = service.create(input);

        assertThat(result.getId()).isEqualTo("cust-001");
        assertThat(result.getName()).isEqualTo("Saanvi Sharma");
        verify(repository).save(any(Customer.class));
    }

    @Test
    void create_defaultsStatusToProspect() {
        input.setStatus(null);
        when(partyClient.individualExists("party-001")).thenReturn(true);
        when(repository.save(any(Customer.class))).thenAnswer(inv -> {
            Customer c = inv.getArgument(0);
            assertThat(c.getStatus()).isEqualTo("prospect");
            return saved;
        });

        service.create(input);
    }

    @Test
    void create_setsCreatedAt() {
        when(partyClient.individualExists("party-001")).thenReturn(true);
        when(repository.save(any(Customer.class))).thenAnswer(inv -> {
            Customer c = inv.getArgument(0);
            assertThat(c.getCreatedAt()).isNotNull();
            return saved;
        });

        service.create(input);
    }

    @Test
    void create_firesWelcomeNotification() {
        when(partyClient.individualExists("party-001")).thenReturn(true);
        when(repository.save(any(Customer.class))).thenReturn(saved);

        service.create(input);

        verify(communicationClient).sendWelcome("cust-001", "Saanvi Sharma");
    }

    @Test
    void create_unknownParty_throws422() {
        when(partyClient.individualExists("party-001")).thenReturn(false);

        assertThatThrownBy(() -> service.create(input))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("party-001");

        verify(repository, never()).save(any());
        verify(communicationClient, never()).sendWelcome(any(), any());
    }

    @Test
    void create_nullEngagedPartyId_skipsPartyCheck() {
        input.setEngagedPartyId(null);
        when(repository.save(any(Customer.class))).thenReturn(saved);

        service.create(input);

        verify(partyClient, never()).individualExists(any());
    }

    // ── findAll ───────────────────────────────────────────────────────────────

    @Test
    void findAll_returnsAllCustomers() {
        when(repository.findAll()).thenReturn(List.of(saved));

        List<Customer> result = service.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo("cust-001");
    }

    // ── findById ──────────────────────────────────────────────────────────────

    @Test
    void findById_found_returnsCustomer() {
        when(repository.findById("cust-001")).thenReturn(Optional.of(saved));

        assertThat(service.findById("cust-001")).contains(saved);
    }

    @Test
    void findById_notFound_returnsEmpty() {
        when(repository.findById("bad-id")).thenReturn(Optional.empty());

        assertThat(service.findById("bad-id")).isEmpty();
    }

    // ── findByEngagedPartyId ──────────────────────────────────────────────────

    @Test
    void findByEngagedPartyId_found_returnsCustomer() {
        when(repository.findByEngagedPartyId("party-001")).thenReturn(Optional.of(saved));

        assertThat(service.findByEngagedPartyId("party-001")).contains(saved);
    }

    @Test
    void findByEngagedPartyId_notFound_returnsEmpty() {
        when(repository.findByEngagedPartyId("party-999")).thenReturn(Optional.empty());

        assertThat(service.findByEngagedPartyId("party-999")).isEmpty();
    }

    // ── patch ─────────────────────────────────────────────────────────────────

    @Test
    void patch_updatesOnlyNonNullFields() {
        Customer existing = new Customer("Old Name", "party-001");
        existing.setId("cust-001");
        existing.setStatus("prospect");

        Customer patch = new Customer();
        patch.setStatus("active");
        patch.setCustomerRank("Gold");

        when(repository.findById("cust-001")).thenReturn(Optional.of(existing));
        when(repository.save(any(Customer.class))).thenAnswer(inv -> inv.getArgument(0));

        Optional<Customer> result = service.patch("cust-001", patch);

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Old Name");   // unchanged
        assertThat(result.get().getStatus()).isEqualTo("active");   // updated
        assertThat(result.get().getCustomerRank()).isEqualTo("Gold"); // updated
    }

    @Test
    void patch_notFound_returnsEmpty() {
        when(repository.findById("bad-id")).thenReturn(Optional.empty());

        assertThat(service.patch("bad-id", new Customer())).isEmpty();
        verify(repository, never()).save(any());
    }

    // ── delete ────────────────────────────────────────────────────────────────

    @Test
    void delete_exists_deletesAndReturnsTrue() {
        when(repository.existsById("cust-001")).thenReturn(true);

        assertThat(service.delete("cust-001")).isTrue();
        verify(repository).deleteById("cust-001");
    }

    @Test
    void delete_notFound_returnsFalseWithoutDelete() {
        when(repository.existsById("bad-id")).thenReturn(false);

        assertThat(service.delete("bad-id")).isFalse();
        verify(repository, never()).deleteById(any());
    }
}
