package com.jio.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorRepresentation {
    private String code;
    private String reason;
    private String message;
    private String status;
    private String referenceError;
    private OffsetDateTime timestamp;
}
