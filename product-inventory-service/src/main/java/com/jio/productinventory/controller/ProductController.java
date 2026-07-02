package com.jio.productinventory.controller;

import com.jio.productinventory.dto.*;
import com.jio.productinventory.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;

/**
 * TMF637 Product Inventory Management API — REST Controller.
 * Base path: /tmf-api/productInventoryManagement/v4
 */
@RestController
@RequestMapping("/tmf-api/productInventoryManagement/v4")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "product", description = "TMF637 Product Inventory Management")
public class ProductController {

    private final ProductService productService;

    // ── POST /product ────────────────────────────────────────

    @PostMapping("/product")
    @Operation(summary = "Creates a Product",
               description = "Creates a new Product resource per TMF637. Status is mandatory.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Created"),
        @ApiResponse(responseCode = "400", description = "Bad Request"),
        @ApiResponse(responseCode = "422", description = "Unprocessable Entity")
    })
    public ResponseEntity<ProductDto> createProduct(
            @Valid @RequestBody ProductCreateDto dto) {

        ProductDto created = productService.createProduct(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    // ── GET /product ─────────────────────────────────────────

    @GetMapping("/product")
    @Operation(summary = "List or find Product objects",
               description = "Retrieves a list of Products. Supports filtering, pagination, field projection and sorting.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Success"),
        @ApiResponse(responseCode = "400", description = "Bad Request")
    })
    public ResponseEntity<List<ProductDto>> listProducts(
            @Parameter(description = "Filter by status") @RequestParam(required = false) String status,
            @Parameter(description = "Filter by customer party ID") @RequestParam(required = false) String customerId,
            @Parameter(description = "Filter by productOfferingId") @RequestParam(required = false) String productOfferingId,
            @Parameter(description = "Filter by billingAccountId") @RequestParam(required = false) String billingAccountId,
            @Parameter(description = "Filter by productOrderId") @RequestParam(required = false) String productOrderId,
            @Parameter(description = "Filter by name (partial match)") @RequestParam(required = false) String name,
            @Parameter(description = "Filter bundles") @RequestParam(required = false) Boolean isBundle,
            @Parameter(description = "Start date from (ISO8601)") @RequestParam(required = false)
                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime startDateFrom,
            @Parameter(description = "Start date to (ISO8601)") @RequestParam(required = false)
                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime startDateTo,
            @Parameter(description = "Comma-separated field names to include") @RequestParam(required = false) String fields,
            @Parameter(description = "Requested index for start of resources (0-based)") @RequestParam(defaultValue = "0") int offset,
            @Parameter(description = "Requested number of resources") @RequestParam(defaultValue = "20") int limit,
            @Parameter(description = "Sort fields, prefix - for DESC (e.g. name,-startDate)") @RequestParam(required = false) String sort) {

        if (limit < 1 || limit > 1000) limit = 20;
        if (offset < 0) offset = 0;

        Page<ProductDto> page = productService.listProducts(
                status, customerId, productOfferingId, billingAccountId,
                productOrderId, name, isBundle, startDateFrom, startDateTo,
                fields, offset, limit, sort);

        return ResponseEntity.ok()
                .header("X-Result-Count", String.valueOf(page.getNumberOfElements()))
                .header("X-Total-Count",  String.valueOf(page.getTotalElements()))
                .body(page.getContent());
    }

    // ── GET /product/{id} ────────────────────────────────────

    @GetMapping("/product/{id}")
    @Operation(summary = "Retrieves a Product by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Success"),
        @ApiResponse(responseCode = "404", description = "Not Found")
    })
    public ResponseEntity<ProductDto> getProduct(
            @PathVariable String id,
            @Parameter(description = "Comma-separated field names") @RequestParam(required = false) String fields) {

        return ResponseEntity.ok(productService.getProduct(id, fields));
    }

    // ── PATCH /product/{id} ──────────────────────────────────

    @PatchMapping("/product/{id}")
    @Operation(summary = "Updates partially a Product",
               description = "Applies a partial update. Only provided fields are modified. " +
                             "Status transitions are validated against the TMF637 lifecycle.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Updated"),
        @ApiResponse(responseCode = "400", description = "Bad Request"),
        @ApiResponse(responseCode = "404", description = "Not Found"),
        @ApiResponse(responseCode = "409", description = "Conflict (optimistic lock)"),
        @ApiResponse(responseCode = "422", description = "Invalid status transition")
    })
    public ResponseEntity<ProductDto> patchProduct(
            @PathVariable String id,
            @Valid @RequestBody ProductUpdateDto dto) {

        return ResponseEntity.ok(productService.patchProduct(id, dto));
    }

    // ── DELETE /product/{id} ─────────────────────────────────

    @DeleteMapping("/product/{id}")
    @Operation(summary = "Deletes a Product")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Deleted"),
        @ApiResponse(responseCode = "404", description = "Not Found")
    })
    public ResponseEntity<Void> deleteProduct(@PathVariable String id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
