package com.jio.productinventory.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

/** TMF637 §Characteristic */
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ProductCharacteristicDto {

    @NotBlank
    @JsonProperty("name")
    private String name;

    @JsonProperty("value")
    private Object value;

    @JsonProperty("valueType")
    private String valueType;

    @JsonProperty("@type")
    private String atType;
}
