package com.jio.tmf622.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.jio.tmf622.model.ProductOrder;
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

@Schema(name = "ProductOrderDeleteEventPayload", description = "The event data structure")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-06-17T14:19:02.575985+05:30[Asia/Kolkata]", comments = "Generator version: 7.23.0")
public class ProductOrderDeleteEventPayload {

  private @Nullable ProductOrder productOrder;

  public ProductOrderDeleteEventPayload productOrder(@Nullable ProductOrder productOrder) {
    this.productOrder = productOrder;
    return this;
  }

  /**
   * Get productOrder
   * @return productOrder
   */
  @Valid 
  @Schema(name = "productOrder", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("productOrder")
  public @Nullable ProductOrder getProductOrder() {
    return productOrder;
  }

  @JsonProperty("productOrder")
  public void setProductOrder(@Nullable ProductOrder productOrder) {
    this.productOrder = productOrder;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ProductOrderDeleteEventPayload productOrderDeleteEventPayload = (ProductOrderDeleteEventPayload) o;
    return Objects.equals(this.productOrder, productOrderDeleteEventPayload.productOrder);
  }

  @Override
  public int hashCode() {
    return Objects.hash(productOrder);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ProductOrderDeleteEventPayload {\n");
    sb.append("    productOrder: ").append(toIndentedString(productOrder)).append("\n");
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

