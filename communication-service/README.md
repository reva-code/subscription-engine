# TMF681 Communication Management Service

The `communication-service` is a microservice responsible for dispatching and logging notifications throughout the customer subscription lifecycle. It conforms to the TM Forum **TMF681 Communication Management API (v4.0.0)** specification.

---

## Supported Channels

1.  **Email**: Outbound transactional HTML email notifications routed via SMTP (e.g. Gmail).
2.  **SMS**: Mobile text messages dispatched via the Twilio SMS API.
3.  **WhatsApp**: Outbound chat alerts sent using the Twilio WhatsApp Sandbox gateway.

---

## Project Structure

```
communication-service/
│
├── pom.xml                                 # Maven dependencies & OpenAPI generator plugin
├── TMF681-Communication-v4.0.0.swagger.json # TMF681 swagger contract specification
├── README.md                               # This module documentation
└── src/
    ├── main/
    │   ├── java/                           # Main source code (Adapters, Services, Controllers, Entities)
    │   └── resources/                      # Application configurations and properties
    ├── test/                               # JUnit unit and integration tests
    └── generated/                          # Auto-generated TMF681 API models and interfaces
```

---

## Configuration & Credentials

Outbound email and Twilio configurations are managed in `src/main/resources/application.properties`. You can set environment variables or edit the file fallback defaults directly:

```properties
# Server
server.port=8088

# Database (MariaDB)
spring.datasource.url=${DB_URL:jdbc:mariadb://localhost:3307/jio_communication?createDatabaseIfNotExist=true}
spring.datasource.username=${DB_USERNAME:root}
spring.datasource.password=${DB_PASSWORD:}

# SMTP Email Configuration (Gmail App Password)
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=${MAIL_USERNAME:}
spring.mail.password=${MAIL_PASSWORD:}

# Twilio SMS/WhatsApp Configuration
twilio.account-sid=${TWILIO_ACCOUNT_SID:}
twilio.auth-token=${TWILIO_AUTH_TOKEN:}
twilio.phone-number=${TWILIO_PHONE_NUMBER:}
twilio.whatsapp-number=${TWILIO_WHATSAPP_NUMBER:}
```

---

## API Endpoints

All APIs are base-pathed under `/tmf-api/communicationManagement/v4`.

### 1. Ad-hoc Message Creation
*   **POST** `/communicationMessage`
*   Creates a notification log, renders the body, and dispatches the message via SMTP, Twilio SMS, or Twilio WhatsApp Sandbox.
*   *Required Fields*: `messageType` (`EMAIL`, `SMS`, or `WHATSAPP`), `content`, `receiver`, and `sender`.

### 2. Event-Driven Messaging (Webhook ingress)
*   **POST** `/events`
*   Receives subscription process events (e.g. `PAYMENT_SUCCESS`, `SUBSCRIPTION_CREATED`, `BILL_GENERATED`). Automatically compiles the corresponding HTML or text template and fan-outs notifications to all available contact channels.

### 3. Retrieval and Listing
*   **GET** `/communicationMessage/{id}`: Fetch message details by database UUID.
*   **GET** `/communicationMessage`: List all messages.
*   **GET** `/communicationMessage/customer/{customerId}`: List messages filtered by customer ID.

---

## How to Compile & Verify

### Build and Package
To compile the swagger specs, generate TMF models, and verify Java 17 compatibility:
```bash
mvn clean compile
```

### Run Automated Tests
Runs unit tests (mocker validations) and integration tests (MockMvc endpoint routing and database states verification):
```bash
mvn test
```

### Start the Application
Starts Tomcat on port `8088` (Ensure MariaDB is active on port `3307` and the `jio_communication` schema is available):
```bash
mvn spring-boot:run
```
Once started, Swagger documentation is accessible at:
`http://localhost:8088/swagger-ui/index.html`
