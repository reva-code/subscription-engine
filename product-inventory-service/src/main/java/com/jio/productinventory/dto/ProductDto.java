package com.jio.productinventory.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * TMF637 §Product response DTO.
 * @JsonInclude(NON_NULL) implements the TMF 'fields' projection:
 * null fields are excluded from serialization, and the mapper
 * only populates requested fields when a 'fields' param is present.
 */
@Data @NoArgsConstructor @AllArgsConstructor @Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductDto {

    @JsonProperty("id")
    private String id;

    @JsonProperty("href")
    private String href;

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("isBundle")
    private Boolean isBundle;

    @JsonProperty("isCustomerVisible")
    private Boolean isCustomerVisible;

    @JsonProperty("productSerialNumber")
    private String productSerialNumber;

    @JsonProperty("orderDate")
    private OffsetDateTime orderDate;

    @JsonProperty("startDate")
    private OffsetDateTime startDate;

    @JsonProperty("terminationDate")
    private OffsetDateTime terminationDate;

    @JsonProperty("status")
    private String status;

    @JsonProperty("billingAccount")
    private BillingAccountRefDto billingAccount;

    @JsonProperty("productOffering")
    private ProductOfferingRefDto productOffering;

    @JsonProperty("productSpecification")
    private ProductSpecificationRefDto productSpecification;

    @JsonProperty("productCharacteristic")
    private List<ProductCharacteristicDto> productCharacteristic;

    @JsonProperty("productRelationship")
    private List<ProductRelationshipDto> productRelationship;

    @JsonProperty("productPrice")
    private List<ProductPriceDto> productPrice;

    @JsonProperty("productTerm")
    private List<ProductTermDto> productTerm;

    @JsonProperty("relatedParty")
    private List<RelatedPartyDto> relatedParty;

    @JsonProperty("place")
    private List<ProductPlaceDto> place;

    @JsonProperty("productOrderItem")
    private List<ProductOrderItemDto> productOrderItem;

    @JsonProperty("agreement")
    private List<AgreementRefDto> agreement;

    @JsonProperty("note")
    private List<ProductNoteDto> note;

    @JsonProperty("attachment")
    private List<ProductAttachmentDto> attachment;

    @JsonProperty("realizingResource")
    private List<RealizingResourceDto> realizingResource;

    @JsonProperty("realizingService")
    private List<RealizingServiceDto> realizingService;

    @JsonProperty("@baseType")
    private String atBaseType;

    @JsonProperty("@schemaLocation")
    private String atSchemaLocation;

    @JsonProperty("@type")
    private String atType;

    // ── Nested reference DTOs ─────────────────────────────────

    @Data @NoArgsConstructor @AllArgsConstructor @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ProductOfferingRefDto {
        @JsonProperty("id") private String id;
        @JsonProperty("href") private String href;
        @JsonProperty("name") private String name;
    }

    @Data @NoArgsConstructor @AllArgsConstructor @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ProductSpecificationRefDto {
        @JsonProperty("id") private String id;
        @JsonProperty("href") private String href;
        @JsonProperty("name") private String name;
        @JsonProperty("version") private String version;
    }

    @Data @NoArgsConstructor @AllArgsConstructor @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ProductPlaceDto {
        @JsonProperty("id") private String id;
        @JsonProperty("href") private String href;
        @JsonProperty("name") private String name;
        @JsonProperty("role") private String role;
        @JsonProperty("@type") private String atType;
        @JsonProperty("@referredType") private String atReferredType;
    }

    @Data @NoArgsConstructor @AllArgsConstructor @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ProductOrderItemDto {
        @JsonProperty("productOrderId") private String productOrderId;
        @JsonProperty("productOrderHref") private String productOrderHref;
        @JsonProperty("orderItemId") private String orderItemId;
        @JsonProperty("orderItemAction") private String orderItemAction;
        @JsonProperty("role") private String role;
    }

    @Data @NoArgsConstructor @AllArgsConstructor @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class AgreementRefDto {
        @JsonProperty("id") private String id;
        @JsonProperty("href") private String href;
        @JsonProperty("name") private String name;
        @JsonProperty("agreementItemId") private String agreementItemId;
    }

    @Data @NoArgsConstructor @AllArgsConstructor @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ProductNoteDto {
        @JsonProperty("author") private String author;
        @JsonProperty("date") private OffsetDateTime date;
        @JsonProperty("text") private String text;
    }

    @Data @NoArgsConstructor @AllArgsConstructor @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ProductAttachmentDto {
        @JsonProperty("id") private String id;
        @JsonProperty("href") private String href;
        @JsonProperty("name") private String name;
        @JsonProperty("description") private String description;
        @JsonProperty("url") private String url;
        @JsonProperty("mimeType") private String mimeType;
    }

    @Data @NoArgsConstructor @AllArgsConstructor @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class RealizingResourceDto {
        @JsonProperty("id") private String id;
        @JsonProperty("href") private String href;
        @JsonProperty("name") private String name;
        @JsonProperty("value") private String value;
    }

    @Data @NoArgsConstructor @AllArgsConstructor @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class RealizingServiceDto {
        @JsonProperty("id") private String id;
        @JsonProperty("href") private String href;
        @JsonProperty("name") private String name;
    }
}
