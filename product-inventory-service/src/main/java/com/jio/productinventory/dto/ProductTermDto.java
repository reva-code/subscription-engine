package com.jio.productinventory.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

/** TMF637 §ProductTerm */
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ProductTermDto {

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("duration")
    private QuantityDto duration;

    @JsonProperty("validFor")
    private TimePeriodDto validFor;

    @JsonProperty("@type")
    private String atType;

    @Data @NoArgsConstructor @AllArgsConstructor @Builder
    public static class QuantityDto {
        @JsonProperty("amount")
        private BigDecimal amount;

        @JsonProperty("units")
        private String units;
    }

    @Data @NoArgsConstructor @AllArgsConstructor @Builder
    public static class TimePeriodDto {
        @JsonProperty("startDateTime")
        private OffsetDateTime startDateTime;

        @JsonProperty("endDateTime")
        private OffsetDateTime endDateTime;
    }
}
