package com.jio.productinventory.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jio.productinventory.dto.*;
import com.jio.productinventory.entity.ProductStatusType;
import com.jio.productinventory.repository.ProductRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Task 2: Integration tests for
 *   POST /product     — create a product
 *   GET  /product     — list products with filters
 *   GET  /product/{id} — get by id
 *   PATCH /product/{id} — lifecycle state transitions
 *
 * Uses H2 in-memory database (application-test.yml).
 * Tests the full Spring stack: Controller → Service → Repository.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProductRestIntegrationTest {

    private static final String BASE = "/tmf-api/productInventoryManagement/v4";

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @Autowired ProductRepository productRepository;

    // Shared product ID across ordered tests
    private static String createdProductId;

    // ── POST /product ─────────────────────────────────────────

    @Test
    @Order(1)
    @DisplayName("POST /product — creates product with status=created, returns 201 + Location header")
    void postProduct_creates_returnsCreated() throws Exception {
        ProductCreateDto request = buildCreateRequest("Jio 5G Unlimited",
                "created", "jio-offering-001", "cust-001");

        MvcResult result = mockMvc.perform(post(BASE + "/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.name").value("Jio 5G Unlimited"))
                .andExpect(jsonPath("$.status").value("created"))
                .andExpect(jsonPath("$.href").isNotEmpty())
                .andExpect(jsonPath("$.productOffering.id").value("jio-offering-001"))
                .andExpect(jsonPath("$.relatedParty[0].id").value("cust-001"))
                .andExpect(jsonPath("$.relatedParty[0].role").value("Customer"))
                .andExpect(jsonPath("$.productCharacteristic[0].name").value("MSISDN"))
                .andExpect(jsonPath("$.productCharacteristic[0].value").value("9999900000"))
                .andReturn();

        String body = result.getResponse().getContentAsString();
        createdProductId = objectMapper.readTree(body).get("id").asText();
        assertThat(createdProductId).isNotBlank();
    }

    @Test
    @Order(2)
    @DisplayName("POST /product — rejects request when status is missing, returns 400")
    void postProduct_missingStatus_returns400() throws Exception {
        ProductCreateDto request = ProductCreateDto.builder()
                .name("Bad Request Product")
                // status intentionally omitted
                .build();

        mockMvc.perform(post(BASE + "/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.reason").value("Validation Failed"));
    }

    @Test
    @Order(3)
    @DisplayName("POST /product — rejects invalid status value, returns 400")
    void postProduct_invalidStatus_returns400() throws Exception {
        ProductCreateDto request = ProductCreateDto.builder()
                .name("Bad Status Product")
                .status("FLYING")           // not a valid TMF637 status
                .build();

        mockMvc.perform(post(BASE + "/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"));
    }

    // ── GET /product ──────────────────────────────────────────

    @Test
    @Order(4)
    @DisplayName("GET /product — lists all products, returns X-Total-Count header")
    void getProducts_returnsList() throws Exception {
        mockMvc.perform(get(BASE + "/product")
                        .param("limit", "20")
                        .param("offset", "0"))
                .andExpect(status().isOk())
                .andExpect(header().exists("X-Total-Count"))
                .andExpect(header().exists("X-Result-Count"))
                .andExpect(jsonPath("$", hasSize(greaterThan(0))));
    }

    @Test
    @Order(5)
    @DisplayName("GET /product?status=created — filters by lifecycle status")
    void getProducts_filterByStatus_returnsFiltered() throws Exception {
        mockMvc.perform(get(BASE + "/product")
                        .param("status", "created"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].status", everyItem(is("created"))));
    }

    @Test
    @Order(6)
    @DisplayName("GET /product?customerId=cust-001 — filters products by customer party ID")
    void getProducts_filterByCustomer_returnsFiltered() throws Exception {
        mockMvc.perform(get(BASE + "/product")
                        .param("customerId", "cust-001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
    }

    @Test
    @Order(7)
    @DisplayName("GET /product?productOfferingId=jio-offering-001 — filters by offering")
    void getProducts_filterByOffering_returnsFiltered() throws Exception {
        mockMvc.perform(get(BASE + "/product")
                        .param("productOfferingId", "jio-offering-001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[0].productOffering.id").value("jio-offering-001"));
    }

    @Test
    @Order(8)
    @DisplayName("GET /product?fields=id,status — field projection returns only requested fields")
    void getProducts_fieldProjection_returnsOnlyRequestedFields() throws Exception {
        mockMvc.perform(get(BASE + "/product")
                        .param("fields", "id,status"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].status").exists())
                .andExpect(jsonPath("$[0].name").doesNotExist());
    }

    @Test
    @Order(9)
    @DisplayName("GET /product/{id} — retrieves specific product with all sub-resources")
    void getProductById_returnsFullProduct() throws Exception {
        mockMvc.perform(get(BASE + "/product/" + createdProductId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdProductId))
                .andExpect(jsonPath("$.status").value("created"))
                .andExpect(jsonPath("$.productCharacteristic").isArray())
                .andExpect(jsonPath("$.relatedParty").isArray())
                .andExpect(jsonPath("$.productTerm").isArray());
    }

    @Test
    @Order(10)
    @DisplayName("GET /product/{id} — returns 404 for unknown id")
    void getProductById_notFound_returns404() throws Exception {
        mockMvc.perform(get(BASE + "/product/non-existent-id-xyz"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("404"));
    }

    @Test
    @Order(11)
    @DisplayName("GET /product — pagination: limit=1 returns 1 item, X-Total-Count shows full count")
    void getProducts_pagination_returnsCorrectPage() throws Exception {
        // Create a second product first
        ProductCreateDto second = buildCreateRequest("Jio Broadband 100", "created",
                "jio-offering-002", "cust-002");
        mockMvc.perform(post(BASE + "/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(second)))
                .andExpect(status().isCreated());

        mockMvc.perform(get(BASE + "/product")
                        .param("limit", "1")
                        .param("offset", "0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(header().string("X-Result-Count", "1"))
                .andExpect(header().exists("X-Total-Count"));
    }

    // ── Lifecycle state transitions ───────────────────────────

    @Test
    @Order(12)
    @DisplayName("PATCH /product/{id} — created → pendingActive (valid transition)")
    void patch_createdToPendingActive_succeeds() throws Exception {
        ProductUpdateDto dto = ProductUpdateDto.builder().status("pendingActive").build();

        mockMvc.perform(patch(BASE + "/product/" + createdProductId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("pendingActive"));
    }

    @Test
    @Order(13)
    @DisplayName("PATCH /product/{id} — pendingActive → active (valid transition)")
    void patch_pendingActiveToActive_succeeds() throws Exception {
        ProductUpdateDto dto = ProductUpdateDto.builder().status("active").build();

        mockMvc.perform(patch(BASE + "/product/" + createdProductId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("active"));
    }

    @Test
    @Order(14)
    @DisplayName("PATCH /product/{id} — active → suspended (valid transition)")
    void patch_activeToSuspended_succeeds() throws Exception {
        ProductUpdateDto dto = ProductUpdateDto.builder().status("suspended").build();

        mockMvc.perform(patch(BASE + "/product/" + createdProductId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("suspended"));
    }

    @Test
    @Order(15)
    @DisplayName("PATCH /product/{id} — suspended → active (reactivation, valid transition)")
    void patch_suspendedToActive_succeeds() throws Exception {
        ProductUpdateDto dto = ProductUpdateDto.builder().status("active").build();

        mockMvc.perform(patch(BASE + "/product/" + createdProductId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("active"));
    }

    @Test
    @Order(16)
    @DisplayName("PATCH /product/{id} — active → pendingTerminate → terminated (full offboard flow)")
    void patch_fullTerminationFlow_succeeds() throws Exception {
        // active → pendingTerminate
        mockMvc.perform(patch(BASE + "/product/" + createdProductId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                ProductUpdateDto.builder().status("pendingTerminate").build())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("pendingTerminate"));

        // pendingTerminate → terminated
        mockMvc.perform(patch(BASE + "/product/" + createdProductId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                ProductUpdateDto.builder().status("terminated").build())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("terminated"));
    }

    @Test
    @Order(17)
    @DisplayName("PATCH /product/{id} — terminated → active (INVALID: terminal state) → 422")
    void patch_terminatedToActive_rejectsWithStatusTransitionError() throws Exception {
        ProductUpdateDto dto = ProductUpdateDto.builder().status("active").build();

        mockMvc.perform(patch(BASE + "/product/" + createdProductId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.code").value("422"))
                .andExpect(jsonPath("$.reason").value("Invalid Status Transition"));
    }

    @Test
    @Order(18)
    @DisplayName("PATCH /product/{id} — PATCH non-status fields does not change status")
    void patch_nonStatusFields_doesNotChangeStatus() throws Exception {
        // Create a fresh product in 'created' state
        MvcResult result = mockMvc.perform(post(BASE + "/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                buildCreateRequest("Patch Name Test", "created",
                                        "off-003", "cust-003"))))
                .andExpect(status().isCreated())
                .andReturn();

        String freshId = objectMapper.readTree(
                result.getResponse().getContentAsString()).get("id").asText();

        // Patch only the name
        ProductUpdateDto nameOnly = ProductUpdateDto.builder()
                .name("Updated Product Name")
                .build();

        mockMvc.perform(patch(BASE + "/product/" + freshId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nameOnly)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Product Name"))
                .andExpect(jsonPath("$.status").value("created")); // status unchanged
    }

    // ── DELETE /product ───────────────────────────────────────

    @Test
    @Order(19)
    @DisplayName("DELETE /product/{id} — deletes product, subsequent GET returns 404")
    void deleteProduct_thenGetReturns404() throws Exception {
        // Create a product to delete
        MvcResult result = mockMvc.perform(post(BASE + "/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                buildCreateRequest("To Delete", "created", "off-del", "cust-del"))))
                .andExpect(status().isCreated())
                .andReturn();

        String deleteId = objectMapper.readTree(
                result.getResponse().getContentAsString()).get("id").asText();

        // Delete it
        mockMvc.perform(delete(BASE + "/product/" + deleteId))
                .andExpect(status().isNoContent());

        // Confirm gone
        mockMvc.perform(get(BASE + "/product/" + deleteId))
                .andExpect(status().isNotFound());
    }

    // ── Helpers ───────────────────────────────────────────────

    private ProductCreateDto buildCreateRequest(String name, String status,
                                                 String offeringId, String customerId) {
        return ProductCreateDto.builder()
                .name(name)
                .description("Integration test product — " + name)
                .status(status)
                .isBundle(false)
                .isCustomerVisible(true)
                .productOffering(ProductDto.ProductOfferingRefDto.builder()
                        .id(offeringId)
                        .name("Jio Offering " + offeringId)
                        .href("http://localhost:8081/tmf-api/productCatalogManagement/v4/productOffering/" + offeringId)
                        .build())
                .productSpecification(ProductDto.ProductSpecificationRefDto.builder()
                        .id("spec-001")
                        .name("Mobile Postpaid Spec")
                        .version("1.0")
                        .build())
                .billingAccount(BillingAccountRefDto.builder()
                        .id("ba-001")
                        .name("Default Billing Account")
                        .build())
                .relatedParty(List.of(RelatedPartyDto.builder()
                        .id(customerId)
                        .name("Test Customer")
                        .role("Customer")
                        .atReferredType("Customer")
                        .build()))
                .productCharacteristic(List.of(
                        ProductCharacteristicDto.builder()
                                .name("MSISDN")
                                .value("9999900000")
                                .valueType("string")
                                .build(),
                        ProductCharacteristicDto.builder()
                                .name("dataQuota")
                                .value("unlimited")
                                .valueType("string")
                                .build()))
                .productTerm(List.of(ProductTermDto.builder()
                        .name("12-month commitment")
                        .description("12 month contract lock-in")
                        .duration(ProductTermDto.QuantityDto.builder()
                                .amount(new java.math.BigDecimal("12"))
                                .units("month")
                                .build())
                        .build()))
                .productPrice(List.of(ProductPriceDto.builder()
                        .name("Monthly Recurring Charge")
                        .priceType("recurring")
                        .recurringChargePeriod("monthly")
                        .price(ProductPriceDto.PriceDto.builder()
                                .dutyFreeAmount(MoneyDto.builder()
                                        .value(new java.math.BigDecimal("599.00"))
                                        .unit("INR")
                                        .build())
                                .taxRate(new java.math.BigDecimal("18.00"))
                                .build())
                        .build()))
                .build();
    }
}
