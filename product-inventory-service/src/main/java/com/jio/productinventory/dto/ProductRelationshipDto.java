package com.jio.productinventory.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

/** TMF637 §ProductRelationship */
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ProductRelationshipDto {

    @NotBlank
    @JsonProperty("relationshipType")
    private String relationshipType;

    @JsonProperty("product")
    private ProductRefDto product;

    @JsonProperty("@type")
    private String atType;

    @Data @NoArgsConstructor @AllArgsConstructor @Builder
    public static class ProductRefDto {
        @NotBlank
        @JsonProperty("id")
        private String id;

        @JsonProperty("href")
        private String href;

        @JsonProperty("name")
        private String name;
    }
}
