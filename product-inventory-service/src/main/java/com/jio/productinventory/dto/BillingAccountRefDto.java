package com.jio.productinventory.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

/** TMF637 §BillingAccountRef — link to TMF666 Account Management. */
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class BillingAccountRefDto {

    @NotBlank
    @JsonProperty("id")
    private String id;

    @JsonProperty("href")
    private String href;

    @JsonProperty("name")
    private String name;

    @JsonProperty("@referredType")
    private String atReferredType;
}
