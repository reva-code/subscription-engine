package com.jio.common.model;

import java.time.OffsetDateTime;

public class ErrorRepresentation {
    private String code;
    private String reason;
    private String message;
    private String status;
    private String referenceError;
    private OffsetDateTime timestamp;

    public ErrorRepresentation() {
    }

    public ErrorRepresentation(String code, String reason, String message, String status, String referenceError, OffsetDateTime timestamp) {
        this.code = code;
        this.reason = reason;
        this.message = message;
        this.status = status;
        this.referenceError = referenceError;
        this.timestamp = timestamp;
    }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getReferenceError() { return referenceError; }
    public void setReferenceError(String referenceError) { this.referenceError = referenceError; }

    public OffsetDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(OffsetDateTime timestamp) { this.timestamp = timestamp; }
}
