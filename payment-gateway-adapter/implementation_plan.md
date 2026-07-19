# Implementation Plan - Payment Gateway Adapter

This plan details the design and implementation of the `payment-gateway-adapter` module for the `payment-management-service` using Spring Boot 3.x and Java 17+.

## Key Constraints

* **In-Memory Database**: The database connection points to an **in-memory H2 database** (`jdbc:h2:mem:jio_payment`). This allows the application to run without requiring a local MariaDB instance, while supporting the idempotency checks by persisting webhook event IDs in memory during the application lifecycle.
* **Build tool**: Since standard build tools (`mvn` / `gradle`) are not globally installed, we use a standard Maven `pom.xml` and include Maven Wrapper configuration scripts so the project can be built and run using `./mvnw.cmd`.

---

## Proposed Changes

We created the Spring Boot project structure under the directory `c:\Users\sandh\OneDrive\Desktop\subscription engine\payment gateway integration`.

### 1. Build and Infrastructure Configuration

* **`pom.xml`**:
  * Configured Spring Boot starter parent (version 3.3.0).
  * Added dependencies: `spring-boot-starter-web`, `spring-boot-starter-data-jpa`, `com.h2database:h2`, and `spring-boot-configuration-processor`.
* **`application.yml`**:
  * Set `server.port=8086`.
  * Configured `spring.datasource.url=jdbc:h2:mem:jio_payment;DB_CLOSE_DELAY=-1`.
  * Configured database credentials, Hibernate DDL auto updating, and application-specific gateway configuration blocks (Razorpay, Mock).

---

### 2. Configuration & DTOs

* **`PaymentGatewayProperties.java`**:
  * Maps configuration secrets and gateway options using `@ConfigurationProperties(prefix = "payment.gateway")`.
* **DTO Models**:
  * Defined DTO objects for input/output payloads:
    * `PaymentRequest.java`
    * `PaymentResponse.java`
    * `RefundResponse.java`
    * `GatewayWebhookEvent.java`

---

### 3. Core Strategy Pattern (PaymentGateway)

* **`PaymentGateway.java`**:
  * Core strategy interface.
* **`MockPaymentGateway.java`**:
  * Fully functional mock implementation returning success/failure responses.
* **`RazorpayGatewayAdapter.java`**:
  * Production-ready sandbox integration using RestTemplate and Basic authentication.
* **`GatewayConfig.java`**:
  * Simple factory bean that routes requests based on properties.

---

### 4. Idempotence & Database Logging

* **`ProcessedWebhook.java`**:
  * JPA Entity model containing fields `id`, `eventId` (unique), and `processedAt`.
* **`ProcessedWebhookRepository.java`**:
  * Spring Data Repository to check and insert event IDs.

---

### 5. TMF676 Alignment & Exceptions

* **`GatewayErrorMapper.java`**:
  * Translates raw Razorpay errors into standard TMF676 codes.
* **`PaymentGatewayException.java`**:
  * Standardized exception class.
* **`GlobalExceptionHandler.java`**:
  * `@RestControllerAdvice` mapping exceptions to standard TMF676 formats.

---

### 6. Controller & Webhook Endpoint

* **`PaymentWebhookController.java`**:
  * REST Controller exposed at `/tmf-api/paymentManagement/v4/webhooks/payment`.
  * Handles signature verification, H2 repository checks, and events logic.
