package com.jio.productinventory.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.OffsetDateTime;

/**
 * Response DTO for a single Product lifecycle status transition.
 * Not part of the TMF637 spec, but exposed to customers for
 * subscription status history visibility.
 */
@Data @NoArgsConstructor @AllArgsConstructor @Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductStatusHistoryDto {

    @JsonProperty("fromStatus")
    private String fromStatus;

    @JsonProperty("toStatus")
    private String toStatus;

    @JsonProperty("reason")
    private String reason;

    @JsonProperty("changedAt")
    private OffsetDateTime changedAt;
}
