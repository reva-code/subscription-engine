package com.jio.tmf622.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * An amount in a given unit
 */

@Schema(name = "Quantity", description = "An amount in a given unit")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-06-17T14:19:02.575985+05:30[Asia/Kolkata]", comments = "Generator version: 7.23.0")
public class Quantity {

  private Float amount = 1.0f;

  private @Nullable String units;

  public Quantity amount(Float amount) {
    this.amount = amount;
    return this;
  }

  /**
   * Numeric value in a given unit
   * @return amount
   */
  
  @Schema(name = "amount", description = "Numeric value in a given unit", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("amount")
  public Float getAmount() {
    return amount;
  }

  @JsonProperty("amount")
  public void setAmount(Float amount) {
    this.amount = amount;
  }

  public Quantity units(@Nullable String units) {
    this.units = units;
    return this;
  }

  /**
   * Unit
   * @return units
   */
  
  @Schema(name = "units", description = "Unit", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("units")
  public @Nullable String getUnits() {
    return units;
  }

  @JsonProperty("units")
  public void setUnits(@Nullable String units) {
    this.units = units;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Quantity quantity = (Quantity) o;
    return Objects.equals(this.amount, quantity.amount) &&
        Objects.equals(this.units, quantity.units);
  }

  @Override
  public int hashCode() {
    return Objects.hash(amount, units);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Quantity {\n");
    sb.append("    amount: ").append(toIndentedString(amount)).append("\n");
    sb.append("    units: ").append(toIndentedString(units)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(@Nullable Object o) {
    return o == null ? "null" : o.toString().replace("\n", "\n    ");
  }
}

