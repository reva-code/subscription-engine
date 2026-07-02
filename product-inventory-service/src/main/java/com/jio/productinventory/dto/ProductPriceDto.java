package com.jio.productinventory.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

/** TMF637 §ProductPrice */
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ProductPriceDto {

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    @NotBlank
    @JsonProperty("priceType")
    private String priceType;

    @JsonProperty("recurringChargePeriod")
    private String recurringChargePeriod;

    @JsonProperty("unitOfMeasure")
    private String unitOfMeasure;

    @JsonProperty("price")
    private PriceDto price;

    @JsonProperty("billingAccount")
    private BillingAccountRefDto billingAccount;

    @JsonProperty("productOfferingPrice")
    private ProductOfferingPriceRefDto productOfferingPrice;

    @JsonProperty("productPriceAlteration")
    private List<PriceAlterationDto> productPriceAlteration;

    @JsonProperty("@type")
    private String atType;

    @Data @NoArgsConstructor @AllArgsConstructor @Builder
    public static class PriceDto {
        @JsonProperty("percentage")
        private BigDecimal percentage;

        @JsonProperty("taxRate")
        private BigDecimal taxRate;

        @JsonProperty("dutyFreeAmount")
        private MoneyDto dutyFreeAmount;

        @JsonProperty("taxIncludedAmount")
        private MoneyDto taxIncludedAmount;
    }

    @Data @NoArgsConstructor @AllArgsConstructor @Builder
    public static class ProductOfferingPriceRefDto {
        @JsonProperty("id")
        private String id;

        @JsonProperty("href")
        private String href;

        @JsonProperty("name")
        private String name;
    }

    @Data @NoArgsConstructor @AllArgsConstructor @Builder
    public static class PriceAlterationDto {
        @JsonProperty("name")
        private String name;

        @JsonProperty("description")
        private String description;

        @NotBlank
        @JsonProperty("priceType")
        private String priceType;

        @JsonProperty("applicationDuration")
        private Integer applicationDuration;

        @JsonProperty("priority")
        private Integer priority;

        @JsonProperty("recurringChargePeriod")
        private String recurringChargePeriod;

        @JsonProperty("unitOfMeasure")
        private String unitOfMeasure;

        @JsonProperty("price")
        private PriceDto price;

        @JsonProperty("productOfferingPrice")
        private ProductOfferingPriceRefDto productOfferingPrice;
    }
}
