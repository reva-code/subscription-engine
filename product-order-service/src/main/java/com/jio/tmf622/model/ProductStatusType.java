package com.jio.tmf622.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonValue;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Possible values for the status of the product
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-06-17T14:19:02.575985+05:30[Asia/Kolkata]", comments = "Generator version: 7.23.0")
public enum ProductStatusType {
  
  CREATED("created"),
  
  PENDING_ACTIVE("pendingActive"),
  
  CANCELLED("cancelled"),
  
  ACTIVE("active"),
  
  PENDING_TERMINATE("pendingTerminate"),
  
  TERMINATED("terminated"),
  
  SUSPENDED("suspended"),
  
  ABORTED_("aborted ");

  private final String value;

  ProductStatusType(String value) {
    this.value = value;
  }

  @JsonValue
  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static ProductStatusType fromValue(String value) {
    for (ProductStatusType b : ProductStatusType.values()) {
      if (b.value.equals(value)) {
        return b;
      }
    }
    throw new IllegalArgumentException("Unexpected value '" + value + "'");
  }
}

