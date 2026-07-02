package com.jio.productinventory.exception;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * TM Forum standard error response format.
 * Used by GlobalExceptionHandler for all 4xx/5xx responses.
 */
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class TmfErrorDto {

    @JsonProperty("code")
    private String code;

    @JsonProperty("reason")
    private String reason;

    @JsonProperty("message")
    private String message;

    @JsonProperty("status")
    private Integer status;

    @JsonProperty("referenceError")
    private String referenceError;

    @JsonProperty("@baseType")
    private String atBaseType;

    @JsonProperty("@schemaLocation")
    private String atSchemaLocation;

    @JsonProperty("@type")
    private String atType;
}
