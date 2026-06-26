package com.jio.productinventory.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

/** TMF637 §EventSubscription */
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class EventSubscriptionDto {

    @JsonProperty("id")
    private String id;

    @NotBlank
    @JsonProperty("callback")
    private String callback;

    @JsonProperty("query")
    private String query;
}
