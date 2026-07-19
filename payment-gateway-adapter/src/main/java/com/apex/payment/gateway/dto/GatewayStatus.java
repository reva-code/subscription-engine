package com.apex.payment.gateway.dto;

public enum GatewayStatus {
    PENDING,
    AUTHORIZED,
    CAPTURED,
    REFUNDED,
    FAILED,
    UNKNOWN
}
