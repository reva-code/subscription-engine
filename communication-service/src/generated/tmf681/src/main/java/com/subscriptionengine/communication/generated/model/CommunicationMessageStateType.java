package com.subscriptionengine.communication.generated.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonValue;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Communication message state type
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-07-20T15:01:51.572067+05:30[Asia/Calcutta]", comments = "Generator version: 7.7.0")
public enum CommunicationMessageStateType {
  
  INITIAL("initial"),
  
  IN_PROGRESS("inProgress"),
  
  COMPLETED("completed"),
  
  CANCELLED("cancelled"),
  
  FAILED("failed");

  private String value;

  CommunicationMessageStateType(String value) {
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
  public static CommunicationMessageStateType fromValue(String value) {
    for (CommunicationMessageStateType b : CommunicationMessageStateType.values()) {
      if (b.value.equals(value)) {
        return b;
      }
    }
    throw new IllegalArgumentException("Unexpected value '" + value + "'");
  }
}

