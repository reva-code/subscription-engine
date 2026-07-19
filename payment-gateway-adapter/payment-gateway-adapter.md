# payment-gateway-adapter

## Overview

The `payment-gateway-adapter` module is an integration layer between the TMF676 Payment Management Service and Razorpay.
It provides a single strategy-based payment gateway abstraction, request/response translation, webhook security, idempotency, error mapping, and observability.

## Architecture

- `TMF676 Payment Management Service`
- `payment-gateway-adapter`
  - `PaymentGateway` interface
  - `RazorpayGatewayAdapter`
  - `MockPaymentGateway`
  - `PaymentWebhookController`
  - `GatewayErrorMapper`
  - `ProcessedWebhookRepository`
  - `PaymentGatewayProperties`
- `Razorpay Sandbox / Production`

## Component Responsibilities

### PaymentGateway

Defines the core adapter contract:
- `authorize(PaymentRequest)`
- `capture(String transactionId)`
- `refund(String transactionId, BigDecimal amount)`
- `getStatus(String transactionId)`

### RazorpayGatewayAdapter

Provides the production Razorpay implementation for the gateway contract.
It handles:
- order creation for authorization
- capture with actual Razorpay payment details
- refunds
- transaction status queries
- secure Basic Auth communication with Razorpay

### MockPaymentGateway

Provides a deterministic local implementation for development and unit tests.

### PaymentWebhookController

Handles Razorpay webhook callbacks at `/tmf-api/paymentManagement/v4/webhooks/payment`.
It enforces:
- HMAC-SHA256 signature validation
- payload validation
- duplicate event detection
- idempotency via `processed_webhooks`

### GatewayErrorMapper

Maps Razorpay error codes to TMF676 failure reason codes.

## API Contracts

### Webhook Endpoint

- `POST /tmf-api/paymentManagement/v4/webhooks/payment`
- Required header: `X-Razorpay-Signature`
- Response codes:
  - `200 OK` for successful processing
  - `208 Already Reported` for duplicate events
  - `400 Bad Request` for invalid signature or malformed payload
  - `502 Bad Gateway` for Razorpay capture errors

## Webhook Flow

1. Validate `X-Razorpay-Signature` using the configured webhook secret.
2. Deserialize the payload into `GatewayWebhookEvent`.
3. Validate required fields.
4. Check `ProcessedWebhookRepository.existsByEventId(eventId)`.
5. Persist `ProcessedWebhook` for idempotency.
6. Execute business logic.
7. Return `200 OK` or `208 Already Reported`.

## Error Handling

- `PaymentGatewayException` is the base runtime exception for gateway failures.
- `WebhookValidationException` is used for webhook payload or signature failures.
- `PaymentCaptureException` is used for Razorpay capture lifecycle failures.
- `GlobalExceptionHandler` normalizes responses and includes `correlationId`.

## Deployment Guide

1. Configure `application.yml` or environment-specific overrides.
2. Set `payment.gateway.provider=razorpay`.
3. Set Razorpay credentials under `payment.gateway.razorpay`.
4. Ensure the webhook secret is securely provisioned and not stored in plaintext.
5. Use `spring.datasource` settings for production MariaDB.

## Configuration Guide

Example configuration:

```yaml
payment:
  gateway:
    provider: razorpay
    razorpay:
      key-id: ${RAZORPAY_KEY_ID}
      key-secret: ${RAZORPAY_KEY_SECRET}
      webhook-secret: ${RAZORPAY_WEBHOOK_SECRET}
```

## Testing Guide

- Unit tests cover gateway adapter behavior and webhook validation.
- Integration tests should validate:
  - authorize flow
  - capture flow
  - refund flow
  - status flow
  - webhook signature validation
  - duplicate webhook handling

## Production Hardening

- Use a production-grade database instead of H2.
- Store secrets in a vault or environment variables.
- Enable structured logging with correlation IDs.
- Monitor webhook delivery and replay attempts.
- Use HTTPS for all external communications.
