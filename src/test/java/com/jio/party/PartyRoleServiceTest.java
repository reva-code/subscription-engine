package com.jio.party;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jio.party.entity.PartyRole;
import com.jio.party.exception.ResourceNotFoundException;
import com.jio.party.repository.PartyRoleRepository;
import com.jio.party.service.PartyRoleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PartyRoleServiceTest {

    @Mock
    private PartyRoleRepository repository;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private PartyRoleService service;

    @Test
    void list_noFilters_returnsAll() {
        when(repository.findAll()).thenReturn(List.of(new PartyRole()));
        assertThat(service.list(null, null)).hasSize(1);
        verify(repository).findAll();
    }

    @Test
    void list_roleTypeFilter_returnsByRoleType() {
        when(repository.findByRoleType("ContentProvider")).thenReturn(List.of(new PartyRole()));
        assertThat(service.list("ContentProvider", null)).hasSize(1);
        verify(repository).findByRoleType("ContentProvider");
    }

    @Test
    void list_bothFilters_returnsByRoleTypeAndStatus() {
        when(repository.findByRoleTypeAndStatus("Banking", "Active")).thenReturn(List.of(new PartyRole()));
        assertThat(service.list("Banking", "Active")).hasSize(1);
        verify(repository).findByRoleTypeAndStatus("Banking", "Active");
    }

    @Test
    void findById_exists_returnsRole() {
        PartyRole role = new PartyRole();
        when(repository.findById("id1")).thenReturn(Optional.of(role));
        assertThat(service.findById("id1")).isSameAs(role);
    }

    @Test
    void findById_notFound_throwsResourceNotFoundException() {
        when(repository.findById("missing")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.findById("missing"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("missing");
    }

    @Test
    void delete_notFound_throwsResourceNotFoundException() {
        when(repository.existsById("x")).thenReturn(false);
        assertThatThrownBy(() -> service.delete("x"))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void delete_exists_callsDeleteById() {
        when(repository.existsById("id1")).thenReturn(true);
        service.delete("id1");
        verify(repository).deleteById("id1");
    }
}
