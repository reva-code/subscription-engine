package com.jio.customer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jio.customer.model.Customer;
import com.jio.customer.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    private static final String BASE = "/tmf-api/customerManagement/v4/customer";

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @MockBean  CustomerService service;

    // ── POST ─────────────────────────────────────────────────────────────────

    @Test
    void createCustomer_validBody_returns201() throws Exception {
        Customer input  = new Customer("Saanvi Sharma", "party-001");
        Customer saved  = new Customer("Saanvi Sharma", "party-001");
        saved.setId("cust-001");
        saved.setStatus("prospect");

        when(service.create(any(Customer.class))).thenReturn(saved);

        mockMvc.perform(post(BASE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("cust-001"))
                .andExpect(jsonPath("$.name").value("Saanvi Sharma"))
                .andExpect(jsonPath("$.status").value("prospect"));
    }

    @Test
    void createCustomer_blankName_returns400() throws Exception {
        Customer bad = new Customer("", "party-001");

        mockMvc.perform(post(BASE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bad)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createCustomer_blankEngagedPartyId_returns400() throws Exception {
        Customer bad = new Customer("Saanvi", "");

        mockMvc.perform(post(BASE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(bad)))
                .andExpect(status().isBadRequest());
    }

    // ── GET (list) ────────────────────────────────────────────────────────────

    @Test
    void listCustomer_noFilter_returnsAll() throws Exception {
        Customer c1 = new Customer("Alice", "p-1"); c1.setId("c1"); c1.setStatus("active");
        Customer c2 = new Customer("Bob",   "p-2"); c2.setId("c2"); c2.setStatus("inactive");

        when(service.findAll()).thenReturn(List.of(c1, c2));

        mockMvc.perform(get(BASE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void listCustomer_statusFilter_returnsFiltered() throws Exception {
        Customer active = new Customer("Alice", "p-1"); active.setId("c1"); active.setStatus("active");
        Customer other  = new Customer("Bob",   "p-2"); other.setId("c2");  other.setStatus("inactive");

        when(service.findAll()).thenReturn(List.of(active, other));

        mockMvc.perform(get(BASE).param("status", "active"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Alice"));
    }

    @Test
    void listCustomer_byEngagedPartyId_returnsMatch() throws Exception {
        Customer c = new Customer("Alice", "p-1"); c.setId("c1");

        when(service.findByEngagedPartyId("p-1")).thenReturn(Optional.of(c));

        mockMvc.perform(get(BASE).param("engagedPartyId", "p-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].engagedPartyId").value("p-1"));
    }

    // ── GET /{id} ─────────────────────────────────────────────────────────────

    @Test
    void retrieveCustomer_found_returns200() throws Exception {
        Customer c = new Customer("Alice", "p-1"); c.setId("cust-001");

        when(service.findById("cust-001")).thenReturn(Optional.of(c));

        mockMvc.perform(get(BASE + "/cust-001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("cust-001"));
    }

    @Test
    void retrieveCustomer_notFound_returns404() throws Exception {
        when(service.findById("bad-id")).thenReturn(Optional.empty());

        mockMvc.perform(get(BASE + "/bad-id"))
                .andExpect(status().isNotFound());
    }

    // ── PATCH /{id} ───────────────────────────────────────────────────────────

    @Test
    void patchCustomer_found_returns200WithUpdatedFields() throws Exception {
        Customer patch   = new Customer(); patch.setStatus("active");
        Customer updated = new Customer("Alice", "p-1"); updated.setId("cust-001"); updated.setStatus("active");

        when(service.patch(eq("cust-001"), any(Customer.class))).thenReturn(Optional.of(updated));

        mockMvc.perform(patch(BASE + "/cust-001")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(patch)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("active"));
    }

    @Test
    void patchCustomer_notFound_returns404() throws Exception {
        when(service.patch(eq("bad-id"), any(Customer.class))).thenReturn(Optional.empty());

        mockMvc.perform(patch(BASE + "/bad-id")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isNotFound());
    }

    // ── DELETE /{id} ──────────────────────────────────────────────────────────

    @Test
    void deleteCustomer_exists_returns204() throws Exception {
        when(service.delete("cust-001")).thenReturn(true);

        mockMvc.perform(delete(BASE + "/cust-001"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteCustomer_notFound_returns404() throws Exception {
        when(service.delete("bad-id")).thenReturn(false);

        mockMvc.perform(delete(BASE + "/bad-id"))
                .andExpect(status().isNotFound());
    }
}
