package com.jio.productinventory.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;

/** TMF637 §Money */
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class MoneyDto {
    @JsonProperty("unit")
    private String unit;

    @JsonProperty("value")
    private BigDecimal value;
}
