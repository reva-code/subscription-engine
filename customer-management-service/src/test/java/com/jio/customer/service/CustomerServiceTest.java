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
import org.springframework.http.HttpStatus;
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

    // ── create: middleName ────────────────────────────────────────────────────

    private Customer createWithMiddleName(String middleName) {
        input.setMiddleName(middleName);
        when(partyClient.individualExists("party-001")).thenReturn(true);
        when(repository.save(any(Customer.class))).thenAnswer(inv -> inv.getArgument(0));

        Customer result = service.create(input);

        verify(repository).save(input);
        return result;
    }

    @Test
    void create_withoutMiddleName_savesNull() {
        assertThat(createWithMiddleName(null).getMiddleName()).isNull();
    }

    @Test
    void create_emptyMiddleName_savesNull() {
        assertThat(createWithMiddleName("").getMiddleName()).isNull();
    }

    @Test
    void create_blankMiddleName_savesNull() {
        assertThat(createWithMiddleName("   ").getMiddleName()).isNull();
    }

    @Test
    void create_validMiddleName_isPersisted() {
        assertThat(createWithMiddleName("Mae").getMiddleName()).isEqualTo("Mae");
    }

    @Test
    void create_middleNameOf30Chars_isAccepted() {
        String thirty = "a".repeat(30);

        assertThat(createWithMiddleName(thirty).getMiddleName()).isEqualTo(thirty);
    }

    @Test
    void create_paddedMiddleNameOf30Chars_isTrimmedAndAccepted() {
        String thirty = "a".repeat(30);

        assertThat(createWithMiddleName("  " + thirty + "  ").getMiddleName()).isEqualTo(thirty);
    }

    @Test
    void create_middleNameOf31Chars_throws400WithoutSideEffects() {
        input.setMiddleName("a".repeat(31));

        assertThatThrownBy(() -> service.create(input))
                .isInstanceOfSatisfying(ResponseStatusException.class,
                        e -> assertThat(e.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST));

        verifyNoInteractions(repository, partyClient, communicationClient);
    }

    // ── displayName ───────────────────────────────────────────────────────────

    @Test
    void displayName_insertsMiddleNameBeforeLastWord() {
        Customer customer = new Customer("Anna Rao", "party-001");
        customer.setMiddleName("Mae");

        assertThat(customer.getDisplayName()).isEqualTo("Anna Mae Rao");
        assertThat(customer.getName()).isEqualTo("Anna Rao");
    }

    @Test
    void displayName_threeWordName_insertsBeforeLastWord() {
        Customer customer = new Customer("Anna Maria Rao", "party-001");
        customer.setMiddleName("Mae");

        assertThat(customer.getDisplayName()).isEqualTo("Anna Maria Mae Rao");
    }

    @Test
    void displayName_singleWordName_appendsMiddleName() {
        Customer customer = new Customer("Anna", "party-001");
        customer.setMiddleName("Mae");

        assertThat(customer.getDisplayName()).isEqualTo("Anna Mae");
    }

    @Test
    void displayName_trimsPartsAndCollapsesSpaces() {
        Customer customer = new Customer("  Anna   Rao  ", "party-001");
        customer.setMiddleName("  Mae  ");

        assertThat(customer.getDisplayName()).isEqualTo("Anna Mae Rao");
    }

    @Test
    void displayName_blankName_isMiddleNameOnly() {
        Customer customer = new Customer("  ", "party-001");
        customer.setMiddleName("Mae");

        assertThat(customer.getDisplayName()).isEqualTo("Mae");
    }

    @Test
    void displayName_nullOrBlankMiddleName_equalsNameExactly() {
        Customer customer = new Customer("  Anna   Rao ", "party-001");

        assertThat(customer.getMiddleName()).isNull();
        assertThat(customer.getDisplayName()).isEqualTo("  Anna   Rao ");

        customer.setMiddleName("   ");

        assertThat(customer.getDisplayName()).isEqualTo("  Anna   Rao ");
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
        existing.setMiddleName("Mae");

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
        assertThat(result.get().getMiddleName()).isEqualTo("Mae");  // unchanged
        assertThat(result.get().getEngagedPartyId()).isEqualTo("party-001"); // unchanged
    }

    private Customer patchMiddleName(String middleName) {
        Customer existing = new Customer("Anna Rao", "party-001");
        existing.setMiddleName("Mae");
        Customer patch = new Customer();
        patch.setMiddleName(middleName);

        when(repository.findById("cust-001")).thenReturn(Optional.of(existing));
        when(repository.save(any(Customer.class))).thenAnswer(inv -> inv.getArgument(0));

        Customer result = service.patch("cust-001", patch).orElseThrow();

        verify(repository).save(existing);
        return result;
    }

    @Test
    void patch_validMiddleName_replacesStoredValue() {
        assertThat(patchMiddleName("  Ann ").getMiddleName()).isEqualTo("Ann");
    }

    @Test
    void patch_blankMiddleName_clearsStoredValue() {
        assertThat(patchMiddleName("   ").getMiddleName()).isNull();
    }

    @Test
    void patch_middleNameOf30Chars_isAccepted() {
        String thirty = "a".repeat(30);

        assertThat(patchMiddleName(thirty).getMiddleName()).isEqualTo(thirty);
    }

    @Test
    void patch_middleNameOf31Chars_throws400AndChangesNothing() {
        Customer existing = new Customer("Anna Rao", "party-001");
        existing.setMiddleName("Mae");
        Customer patch = new Customer();
        patch.setName("New Name");
        patch.setMiddleName("a".repeat(31));
        when(repository.findById("cust-001")).thenReturn(Optional.of(existing));

        assertThatThrownBy(() -> service.patch("cust-001", patch))
                .isInstanceOfSatisfying(ResponseStatusException.class,
                        e -> assertThat(e.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST));

        assertThat(existing.getName()).isEqualTo("Anna Rao");
        assertThat(existing.getMiddleName()).isEqualTo("Mae");
        verify(repository, never()).save(any());
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
