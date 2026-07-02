package com.jio.tmf622.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.jio.tmf622.model.CancelProductOrder;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * The event data structure
 */

@Schema(name = "CancelProductOrderCreateEventPayload", description = "The event data structure")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-06-17T14:19:02.575985+05:30[Asia/Kolkata]", comments = "Generator version: 7.23.0")
public class CancelProductOrderCreateEventPayload {

  private @Nullable CancelProductOrder cancelProductOrder;

  public CancelProductOrderCreateEventPayload cancelProductOrder(@Nullable CancelProductOrder cancelProductOrder) {
    this.cancelProductOrder = cancelProductOrder;
    return this;
  }

  /**
   * Get cancelProductOrder
   * @return cancelProductOrder
   */
  @Valid 
  @Schema(name = "cancelProductOrder", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("cancelProductOrder")
  public @Nullable CancelProductOrder getCancelProductOrder() {
    return cancelProductOrder;
  }

  @JsonProperty("cancelProductOrder")
  public void setCancelProductOrder(@Nullable CancelProductOrder cancelProductOrder) {
    this.cancelProductOrder = cancelProductOrder;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CancelProductOrderCreateEventPayload cancelProductOrderCreateEventPayload = (CancelProductOrderCreateEventPayload) o;
    return Objects.equals(this.cancelProductOrder, cancelProductOrderCreateEventPayload.cancelProductOrder);
  }

  @Override
  public int hashCode() {
    return Objects.hash(cancelProductOrder);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CancelProductOrderCreateEventPayload {\n");
    sb.append("    cancelProductOrder: ").append(toIndentedString(cancelProductOrder)).append("\n");
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

