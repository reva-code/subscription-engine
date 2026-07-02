package com.jio.customer.controller;

import com.jio.customer.model.CustomerAccount;
import com.jio.customer.service.CustomerAccountService;
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
 * TMF629 Customer Management API — CustomerAccount resource
 * Base path: /tmf-api/customerManagement/v4/customerAccount
 */
@RestController
@RequestMapping("/tmf-api/customerManagement/v4/customerAccount")
@Tag(name = "CustomerAccount", description = "TMF629 — Manages billing accounts belonging to a Customer")
public class CustomerAccountController {

    private final CustomerAccountService service;

    public CustomerAccountController(CustomerAccountService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Creates a CustomerAccount",
               description = "Request body must include customer.id to link to an existing Customer.")
    @ApiResponse(responseCode = "201", description = "CustomerAccount created")
    @ApiResponse(responseCode = "400", description = "Missing or invalid customer.id")
    @ApiResponse(responseCode = "404", description = "Referenced Customer not found")
    public ResponseEntity<CustomerAccount> createCustomerAccount(
            @Valid @RequestBody CustomerAccount account) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(account));
    }

    @GetMapping
    @Operation(summary = "List or find CustomerAccount objects")
    @ApiResponse(responseCode = "200", description = "Success")
    public ResponseEntity<List<CustomerAccount>> listCustomerAccounts(
            @Parameter(description = "Filter by customer.id")
            @RequestParam(required = false) String customerId,
            @Parameter(description = "Filter by accountType: PREPAID, POSTPAID, ENTERPRISE")
            @RequestParam(required = false) String accountType,
            @Parameter(description = "Filter by paymentStatus: PAID, UNPAID, OVERDUE")
            @RequestParam(required = false) String paymentStatus) {

        if (customerId != null) {
            return ResponseEntity.ok(service.findByCustomerId(customerId));
        }
        List<CustomerAccount> all = service.findAll();
        if (accountType != null) {
            all = all.stream()
                    .filter(a -> accountType.equalsIgnoreCase(a.getAccountType()))
                    .toList();
        }
        if (paymentStatus != null) {
            all = all.stream()
                    .filter(a -> paymentStatus.equalsIgnoreCase(a.getPaymentStatus()))
                    .toList();
        }
        return ResponseEntity.ok(all);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Retrieves a CustomerAccount by ID")
    @ApiResponse(responseCode = "200", description = "Success")
    @ApiResponse(responseCode = "404", description = "Not found")
    public ResponseEntity<CustomerAccount> retrieveCustomerAccount(@PathVariable String id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Updates partially a CustomerAccount",
               description = "Only fields present in the body are updated (PATCH semantics)")
    @ApiResponse(responseCode = "200", description = "Updated")
    @ApiResponse(responseCode = "404", description = "Not found")
    public ResponseEntity<CustomerAccount> patchCustomerAccount(
            @PathVariable String id,
            @RequestBody CustomerAccount patch) {
        return service.patch(id, patch)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletes a CustomerAccount")
    @ApiResponse(responseCode = "204", description = "Deleted")
    @ApiResponse(responseCode = "404", description = "Not found")
    public ResponseEntity<Void> deleteCustomerAccount(@PathVariable String id) {
        return service.delete(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
