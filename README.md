# TMF637 Product Inventory Management Service

> **Jio Subscription Engine** — Microservice for managing active customer products/subscriptions.
> Compliant with [TM Forum TMF637 v4.0.0](https://github.com/tmforum-apis/TMF637_ProductInventory).

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 3.3.13 |
| Persistence | MariaDB 12.3 + Hibernate ORM + Spring Data JPA |
| Migrations | Flyway |
| Mapping | MapStruct 1.5.5 |
| API Docs | SpringDoc OpenAPI 2.6.0 / Swagger UI |
| Testing | JUnit 5 + MockMvc + H2 |
| Build | Maven 3.x |

---

## Task 2 — REST Endpoints & Lifecycle States

### Base URL
```
http://localhost:8084/tmf-api/productInventoryManagement/v4
```

### REST API

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/product` | Create a new product |
| `GET` | `/product` | List/search products |
| `GET` | `/product/{id}` | Get product by ID |
| `PATCH` | `/product/{id}` | Partial update / lifecycle change |
| `DELETE` | `/product/{id}` | Delete a product |
| `POST` | `/hub` | Register event listener |
| `DELETE` | `/hub/{id}` | Unregister event listener |

### Query Parameters (GET /product)

| Parameter | Type | Description |
|---|---|---|
| `status` | string | Filter by lifecycle status |
| `customerId` | string | Filter by related party (customer) |
| `productOfferingId` | string | Filter by catalog offering |
| `billingAccountId` | string | Filter by billing account |
| `productOrderId` | string | Filter by originating order |
| `name` | string | Partial name match |
| `isBundle` | boolean | Filter bundles |
| `startDateFrom` | ISO8601 | Start date range from |
| `startDateTo` | ISO8601 | Start date range to |
| `fields` | string | Comma-separated field projection |
| `offset` | int | Pagination offset (default: 0) |
| `limit` | int | Page size (default: 20, max: 1000) |
| `sort` | string | Sort fields, prefix `-` for DESC |

**Response headers:** `X-Total-Count`, `X-Result-Count`

---

### Lifecycle States (TMF637 §ProductStatusType)

```
                    ┌──────────────┐
         ┌─────────▶│ pendingActive│──────┐
         │          └──────────────┘      │
         │                 │              ▼
    ┌─────────┐            ▼         ┌──────────┐
    │ created │──cancel──▶         │ cancelled│
    └─────────┘         ┌────────┐  └──────────┘
                        │ active │
                        └────────┘
                         │      │
                         ▼      ▼
                   ┌──────────┐  ┌─────────────────┐
                   │suspended │  │ pendingTerminate │
                   └──────────┘  └─────────────────┘
                         │              │
                         └──────┬───────┘
                                ▼
                          ┌────────────┐
                          │ terminated │  (terminal)
                          └────────────┘
```

**Valid transitions:**

| From | To |
|---|---|
| `created` | `pendingActive`, `cancelled` |
| `pendingActive` | `active`, `cancelled`, `aborted` |
| `active` | `suspended`, `pendingTerminate` |
| `suspended` | `active`, `pendingTerminate`, `terminated` |
| `pendingTerminate` | `terminated`, `active` |
| `terminated` | *(terminal — no exit)* |
| `cancelled` | *(terminal — no exit)* |
| `aborted` | *(terminal — no exit)* |

Invalid transitions return **HTTP 422 Unprocessable Entity**.

---

### Sample Requests

**POST /product — Create a mobile subscription:**
```json
POST /tmf-api/productInventoryManagement/v4/product

{
  "name": "Jio 5G Unlimited",
  "status": "created",
  "productOffering": {
    "id": "jio-offering-001",
    "name": "Jio 5G Unlimited Plan"
  },
  "billingAccount": {
    "id": "ba-001",
    "name": "Default Billing Account"
  },
  "relatedParty": [
    { "id": "cust-001", "role": "Customer", "@referredType": "Customer" }
  ],
  "productCharacteristic": [
    { "name": "MSISDN",    "value": "9999900000", "valueType": "string" },
    { "name": "dataQuota", "value": "unlimited",  "valueType": "string" }
  ],
  "productTerm": [
    {
      "name": "12-month commitment",
      "duration": { "amount": 12, "units": "month" }
    }
  ],
  "productPrice": [
    {
      "name": "Monthly Charge",
      "priceType": "recurring",
      "recurringChargePeriod": "monthly",
      "price": {
        "dutyFreeAmount": { "value": 599.00, "unit": "INR" },
        "taxRate": 18.00
      }
    }
  ]
}
```

**Response: 201 Created**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "href": "http://localhost:8084/tmf-api/productInventoryManagement/v4/product/550e8400...",
  "name": "Jio 5G Unlimited",
  "status": "created",
  ...
}
```

**PATCH /product/{id} — Activate the product:**
```json
PATCH /tmf-api/productInventoryManagement/v4/product/550e8400...

{ "status": "pendingActive" }
```

---

## Running Locally

```bash
# 1. Start MariaDB
docker run -d --name mariadb \
  -e MYSQL_ROOT_PASSWORD=root \
  -e MYSQL_DATABASE=jio_product_inventory \
  -p 3306:3306 mariadb:10.11

# 2. Run the service
cd product-inventory-service
mvn spring-boot:run

# 3. Open Swagger UI
open http://localhost:8084/tmf-api/productInventoryManagement/v4/swagger-ui.html
```

## Running Tests

```bash
mvn test
```

Tests use H2 in-memory database — no MariaDB required for testing.

---

## Integration with Other Services

| Service | Port | TMF API |
|---|---|---|
| product-catalog-service | 8081 | TMF620 |
| product-order-service | 8080 | TMF622 |
| party-role-service | 8082 | TMF632 |
| customer-management-service | 8083 | TMF629 |
| **product-inventory-service** | **8084** | **TMF637** |
| usage-management-service | 8085 | TMF635 |
| payment-management-service | 8086 | TMF676 |
