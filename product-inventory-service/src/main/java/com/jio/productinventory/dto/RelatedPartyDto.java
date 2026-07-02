package com.jio.productinventory.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

/** TMF637 §RelatedParty */
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class RelatedPartyDto {

    @NotBlank
    @JsonProperty("id")
    private String id;

    @JsonProperty("href")
    private String href;

    @JsonProperty("name")
    private String name;

    @NotBlank
    @JsonProperty("role")
    private String role;

    @NotBlank
    @JsonProperty("@referredType")
    private String atReferredType;

    @JsonProperty("@type")
    private String atType;
}
