package com.apex.payment.gateway.gateway;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.apex.payment.gateway.config.PaymentGatewayProperties;
import com.apex.payment.gateway.dto.GatewayStatus;
import com.apex.payment.gateway.dto.PaymentRequest;
import com.apex.payment.gateway.dto.PaymentResponse;
import com.apex.payment.gateway.dto.RefundResponse;
import com.apex.payment.gateway.exception.PaymentCaptureException;
import com.apex.payment.gateway.exception.PaymentGatewayException;
import com.apex.payment.gateway.mapper.GatewayErrorMapper;

@Component("razorpayGatewayAdapter")
public class RazorpayGatewayAdapter implements PaymentGateway {

    private static final Logger log = LoggerFactory.getLogger(RazorpayGatewayAdapter.class);
    private static final String RAZORPAY_API_BASE = "https://api.razorpay.com/v1";

    private final PaymentGatewayProperties properties;
    private final GatewayErrorMapper errorMapper;
    private final RestTemplate restTemplate;

    public RazorpayGatewayAdapter(PaymentGatewayProperties properties, GatewayErrorMapper errorMapper, RestTemplate restTemplate) {
        this.properties = properties;
        this.errorMapper = errorMapper;
        this.restTemplate = restTemplate;
    }

    @Override
    public PaymentResponse authorize(PaymentRequest request) {
        log.info("Initiating authorization with Razorpay for amount: {} {}", request.getAmount(), request.getCurrency());

        String url = RAZORPAY_API_BASE + "/orders";
        long amountInPaise = request.getAmount().multiply(new BigDecimal("100")).longValue();

        Map<String, Object> body = new HashMap<>();
        body.put("amount", amountInPaise);
        body.put("currency", request.getCurrency());
        body.put("receipt", "receipt_" + UUID.randomUUID().toString().substring(0, 8));

        Map<String, Object> notes = new HashMap<>();
        notes.put("paymentMethod", request.getPaymentMethod());
        notes.put("customerPhone", request.getCustomerPhone());
        if (request.getDescription() != null) {
            notes.put("description", request.getDescription());
        }
        if (request.getCustomerEmail() != null) {
            notes.put("customerEmail", request.getCustomerEmail());
        }
        body.put("notes", notes);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, createHeaders());

        try {
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);
            Map<String, Object> responseBody = response.getBody();

            if (responseBody != null && responseBody.containsKey("id")) {
                String orderId = (String) responseBody.get("id");
                log.info("Razorpay order created successfully: {}", orderId);
                return PaymentResponse.builder()
                        .transactionId(orderId)
                        .status(GatewayStatus.AUTHORIZED)
                        .amount(request.getAmount())
                        .currency(request.getCurrency())
                        .rawPayload(responseBody)
                        .build();
            }
            throw new PaymentGatewayException("Failed to generate order ID from Razorpay", "ERR_GATEWAY_BAD_RESPONSE", "PAYMENT_DECLINED");
        } catch (HttpStatusCodeException ex) {
            throw handleHttpException("Authorization failed", ex);
        } catch (Exception ex) {
            throw handleGenericException("Authorization communication failed", ex);
        }
    }

    @Override
    public PaymentResponse capture(String transactionId) {
        log.info("Initiating capture for transaction: {}", transactionId);

        Map<String, Object> paymentDetails = retrievePaymentDetails(transactionId);
        String paymentStatus = (String) paymentDetails.get("status");
        if (!"authorized".equalsIgnoreCase(paymentStatus) && !"created".equalsIgnoreCase(paymentStatus)) {
            throw new PaymentCaptureException("Transaction is not in a capturable state: " + paymentStatus, "ERR_CAPTURE_INVALID_STATE", "INVALID_PAYMENT_METHOD");
        }

        Long amountInPaise = extractLongValue(paymentDetails.get("amount"));
        String currency = (String) paymentDetails.get("currency");

        if (amountInPaise == null || amountInPaise <= 0) {
            throw new PaymentCaptureException("Capture amount is missing or invalid for transaction: " + transactionId, "ERR_CAPTURE_AMOUNT_INVALID", "INVALID_PAYMENT_METHOD");
        }

        String url = RAZORPAY_API_BASE + "/payments/" + transactionId + "/capture";
        Map<String, Object> body = new HashMap<>();
        body.put("amount", amountInPaise);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, createHeaders());

        try {
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);
            Map<String, Object> responseBody = response.getBody();

            return PaymentResponse.builder()
                    .transactionId(transactionId)
                    .status(GatewayStatus.CAPTURED)
                    .amount(BigDecimal.valueOf(amountInPaise, 2))
                    .currency(currency)
                    .rawPayload(responseBody)
                    .build();
        } catch (HttpStatusCodeException ex) {
            throw handleHttpException("Capture failed", ex);
        } catch (Exception ex) {
            throw new PaymentCaptureException("Capture communication failed for transaction: " + transactionId, "ERR_CAPTURE_COMMUNICATION", "PAYMENT_TIMEOUT", ex);
        }
    }

    @Override
    public RefundResponse refund(String transactionId, BigDecimal amount) {
        log.info("Initiating refund for transaction: {} of amount: {}", transactionId, amount);

        Map<String, Object> paymentDetails = retrievePaymentDetails(transactionId);
        String currency = (String) paymentDetails.get("currency");

        String url = RAZORPAY_API_BASE + "/payments/" + transactionId + "/refund";
        long amountInPaise = amount.multiply(new BigDecimal("100")).longValue();
        Map<String, Object> body = new HashMap<>();
        body.put("amount", amountInPaise);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, createHeaders());

        try {
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);
            Map<String, Object> responseBody = response.getBody();
            String refundId = responseBody != null ? (String) responseBody.get("id") : "unknown_refund";

            return RefundResponse.builder()
                    .refundId(refundId)
                    .originalTransactionId(transactionId)
                    .amount(amount)
                    .currency(currency)
                    .status(GatewayStatus.REFUNDED)
                    .rawPayload(responseBody)
                    .build();
        } catch (HttpStatusCodeException ex) {
            throw handleHttpException("Refund failed", ex);
        } catch (Exception ex) {
            throw handleGenericException("Refund communication failed", ex);
        }
    }

    @Override
    public GatewayStatus getStatus(String transactionId) {
        log.info("Checking transaction status: {}", transactionId);

        String url = RAZORPAY_API_BASE + "/payments/" + transactionId;
        HttpEntity<Void> entity = new HttpEntity<>(createHeaders());

        try {
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
            Map<String, Object> responseBody = response.getBody();

            if (responseBody != null && responseBody.containsKey("status")) {
                String statusStr = (String) responseBody.get("status");
                return mapGatewayStatus(statusStr);
            }
            return GatewayStatus.UNKNOWN;
        } catch (Exception ex) {
            log.error("Failed to query status for transaction: {}", transactionId, ex);
            return GatewayStatus.UNKNOWN;
        }
    }

    private Map<String, Object> retrievePaymentDetails(String transactionId) {
        String url = RAZORPAY_API_BASE + "/payments/" + transactionId;
        HttpEntity<Void> entity = new HttpEntity<>(createHeaders());

        try {
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
            Map<String, Object> body = response.getBody();
            if (body == null) {
                throw new PaymentCaptureException("Failed to retrieve payment details for transaction: " + transactionId, "ERR_CAPTURE_PAYMENT_NOT_FOUND", "PAYMENT_DECLINED");
            }
            return body;
        } catch (HttpStatusCodeException ex) {
            throw handleHttpException("Failed to retrieve payment details", ex);
        } catch (Exception ex) {
            throw new PaymentCaptureException("Failed to retrieve payment details for transaction: " + transactionId, "ERR_CAPTURE_PAYMENT_RETRIEVAL", "PAYMENT_TIMEOUT", ex);
        }
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (properties.getRazorpay().getKeyId() == null || properties.getRazorpay().getKeySecret() == null) {
            throw new IllegalArgumentException("Razorpay API credentials are not configured.");
        }
        headers.setBasicAuth(properties.getRazorpay().getKeyId(), properties.getRazorpay().getKeySecret());
        return headers;
    }

    private Long extractLongValue(Object value) {
        if (value instanceof Number) {
            return ((Number) value).longValue();
        }
        if (value instanceof String) {
            try {
                return Long.parseLong((String) value);
            } catch (NumberFormatException ignored) {
                return null;
            }
        }
        return null;
    }

    private GatewayStatus mapGatewayStatus(String razorpayStatus) {
        if (razorpayStatus == null) {
            return GatewayStatus.UNKNOWN;
        }
        switch (razorpayStatus.toLowerCase()) {
            case "created":
            case "pending":
                return GatewayStatus.PENDING;
            case "authorized":
                return GatewayStatus.AUTHORIZED;
            case "captured":
                return GatewayStatus.CAPTURED;
            case "refunded":
                return GatewayStatus.REFUNDED;
            case "failed":
                return GatewayStatus.FAILED;
            default:
                return GatewayStatus.UNKNOWN;
        }
    }

    private PaymentGatewayException handleHttpException(String messagePrefix, HttpStatusCodeException ex) {
        log.error("Razorpay API returned error: {} - {}", ex.getStatusCode(), ex.getResponseBodyAsString());
        String errorCode = "ERR_GATEWAY_FAILURE";
        String errorDesc = "Payment declined or invalid request";

        try {
            String body = ex.getResponseBodyAsString();
            if (body.contains("\"code\"")) {
                int codeStart = body.indexOf("\"code\"") + 8;
                int codeEnd = body.indexOf("\"", codeStart);
                errorCode = body.substring(codeStart, codeEnd);
            }
            if (body.contains("\"description\"")) {
                int descStart = body.indexOf("\"description\"") + 15;
                int descEnd = body.indexOf("\"", descStart);
                errorDesc = body.substring(descStart, descEnd);
            }
        } catch (Exception parseEx) {
            log.warn("Failed to parse Razorpay error body", parseEx);
        }

        String tmfReason = errorMapper.mapToTmfReason(errorCode);
        return new PaymentGatewayException(messagePrefix + ": " + errorDesc, errorCode, tmfReason, ex);
    }

    private PaymentGatewayException handleGenericException(String message, Exception ex) {
        log.error("Gateway integration communication failure", ex);
        return new PaymentGatewayException(message, "ERR_CONNECTION_FAILURE", "PAYMENT_TIMEOUT", ex);
    }
}
