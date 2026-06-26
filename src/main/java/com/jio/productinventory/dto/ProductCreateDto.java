package com.jio.productinventory.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * TMF637 §Product_Create — POST /product request body.
 * id and href excluded (server-generated).
 * status is required per spec.
 */
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ProductCreateDto {

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

    @NotNull(message = "status is required")
    @JsonProperty("status")
    private String status;

    @Valid
    @JsonProperty("billingAccount")
    private BillingAccountRefDto billingAccount;

    @Valid
    @JsonProperty("productOffering")
    private ProductDto.ProductOfferingRefDto productOffering;

    @Valid
    @JsonProperty("productSpecification")
    private ProductDto.ProductSpecificationRefDto productSpecification;

    @Valid
    @JsonProperty("productCharacteristic")
    private List<ProductCharacteristicDto> productCharacteristic;

    @Valid
    @JsonProperty("productRelationship")
    private List<ProductRelationshipDto> productRelationship;

    @Valid
    @JsonProperty("productPrice")
    private List<ProductPriceDto> productPrice;

    @Valid
    @JsonProperty("productTerm")
    private List<ProductTermDto> productTerm;

    @Valid
    @JsonProperty("relatedParty")
    private List<RelatedPartyDto> relatedParty;

    @Valid
    @JsonProperty("place")
    private List<ProductDto.ProductPlaceDto> place;

    @Valid
    @JsonProperty("productOrderItem")
    private List<ProductDto.ProductOrderItemDto> productOrderItem;

    @Valid
    @JsonProperty("agreement")
    private List<ProductDto.AgreementRefDto> agreement;

    @Valid
    @JsonProperty("note")
    private List<ProductDto.ProductNoteDto> note;

    @Valid
    @JsonProperty("attachment")
    private List<ProductDto.ProductAttachmentDto> attachment;

    @Valid
    @JsonProperty("realizingResource")
    private List<ProductDto.RealizingResourceDto> realizingResource;

    @Valid
    @JsonProperty("realizingService")
    private List<ProductDto.RealizingServiceDto> realizingService;

    @JsonProperty("@baseType")
    private String atBaseType;

    @JsonProperty("@schemaLocation")
    private String atSchemaLocation;

    @JsonProperty("@type")
    private String atType;
}
