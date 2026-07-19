package com.apex.payment.gateway.mapper;

import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;

@Component
public class GatewayErrorMapper {

    private static final Map<String, String> ERROR_MAP = new HashMap<>();

    static {
        ERROR_MAP.put("payment_failed", "PAYMENT_DECLINED");
        ERROR_MAP.put("insufficient_funds", "INSUFFICIENT_FUNDS");
        ERROR_MAP.put("invalid_card", "INVALID_PAYMENT_METHOD");
        ERROR_MAP.put("gateway_timeout", "PAYMENT_TIMEOUT");
        ERROR_MAP.put("fraud_blocked", "PAYMENT_REJECTED");
        
        ERROR_MAP.put("BAD_REQUEST_ERROR", "INVALID_PAYMENT_METHOD");
        ERROR_MAP.put("GATEWAY_ERROR", "PAYMENT_TIMEOUT");
        ERROR_MAP.put("SERVER_ERROR", "PAYMENT_TIMEOUT");
    }

    public String mapToTmfReason(String gatewayError) {
        if (gatewayError == null) {
            return "PAYMENT_DECLINED";
        }
        
        String mapped = ERROR_MAP.get(gatewayError);
        if (mapped != null) {
            return mapped;
        }

        String normalized = gatewayError.toLowerCase().replace(" ", "_").replace("-", "_");
        for (Map.Entry<String, String> entry : ERROR_MAP.entrySet()) {
            if (normalized.contains(entry.getKey().toLowerCase())) {
                return entry.getValue();
            }
        }

        return "PAYMENT_DECLINED";
    }
}
