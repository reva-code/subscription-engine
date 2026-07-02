package com.jio.party.exception;

public class ErrorResponse {

    private String code;
    private String reason;
    private String message;
    private String status;

    public ErrorResponse(String code, String reason, String message, String status) {
        this.code = code;
        this.reason = reason;
        this.message = message;
        this.status = status;
    }

    public String getCode() { return code; }
    public String getReason() { return reason; }
    public String getMessage() { return message; }
    public String getStatus() { return status; }
}
