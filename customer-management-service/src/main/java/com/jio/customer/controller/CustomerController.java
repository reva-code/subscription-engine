package com.jio.customer.controller;

import com.jio.customer.model.Customer;
import com.jio.customer.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * TMF629 Customer Management API — Customer resource
 * Base path: /tmf-api/customerManagement/v4/customer
 */
@RestController
@RequestMapping("/tmf-api/customerManagement/v4/customer")
@Tag(name = "Customer", description = "TMF629 — Manages customer resources (the commercial identity of a party)")
public class CustomerController {

    private final CustomerService service;

    public CustomerController(CustomerService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Creates a Customer",
               description = "Registers a Party as a Jio Customer. Requires engagedPartyId from TMF632.")
    @ApiResponse(responseCode = "201", description = "Customer created")
    @ApiResponse(responseCode = "400", description = "Validation error")
    public ResponseEntity<Customer> createCustomer(@Valid @RequestBody Customer customer) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(customer));
    }

    @GetMapping
    @Operation(summary = "List or find Customer objects")
    @ApiResponse(responseCode = "200", description = "Success")
    public ResponseEntity<List<Customer>> listCustomer(
            @Parameter(description = "Filter by status: active, inactive, prospect, former")
            @RequestParam(required = false) String status,
            @Parameter(description = "Filter by engagedPartyId (from TMF632)")
            @RequestParam(required = false) String engagedPartyId) {

        if (engagedPartyId != null) {
            return service.findByEngagedPartyId(engagedPartyId)
                    .map(c -> ResponseEntity.ok(List.of(c)))
                    .orElse(ResponseEntity.ok(List.of()));
        }
        List<Customer> result = (status != null)
                ? service.findAll().stream()
                    .filter(c -> status.equalsIgnoreCase(c.getStatus()))
                    .toList()
                : service.findAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Retrieves a Customer by ID")
    @ApiResponse(responseCode = "200", description = "Success")
    @ApiResponse(responseCode = "404", description = "Not found")
    public ResponseEntity<Customer> retrieveCustomer(@PathVariable String id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Updates partially a Customer",
               description = "Only fields present in the body are updated (PATCH semantics)")
    @ApiResponse(responseCode = "200", description = "Updated")
    @ApiResponse(responseCode = "404", description = "Not found")
    public ResponseEntity<Customer> patchCustomer(
            @PathVariable String id,
            @RequestBody Customer patch) {
        return service.patch(id, patch)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletes a Customer")
    @ApiResponse(responseCode = "204", description = "Deleted")
    @ApiResponse(responseCode = "404", description = "Not found")
    public ResponseEntity<Void> deleteCustomer(@PathVariable String id) {
        return service.delete(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
