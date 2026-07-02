# ── Stage 1: Build ──────────────────────────────────────────────────────────
FROM maven:3.9.9-eclipse-temurin-17-alpine AS build
WORKDIR /workspace

COPY pom.xml ./
COPY module-1-customer/party-management-service/pom.xml module-1-customer/party-management-service/pom.xml
COPY module-1-customer/customer-management-service/pom.xml module-1-customer/customer-management-service/pom.xml
COPY module-2-product/product-catalog-service/pom.xml module-2-product/product-catalog-service/pom.xml
COPY module-2-product/product-inventory-service/pom.xml module-2-product/product-inventory-service/pom.xml
COPY module-2-product/product-order-service/pom.xml module-2-product/product-order-service/pom.xml
COPY module-3-usage/usage-management-service/pom.xml module-3-usage/usage-management-service/pom.xml
COPY module-4-payments/payment-method-service/pom.xml module-4-payments/payment-method-service/pom.xml
COPY module-4-payments/payment-management-service/pom.xml module-4-payments/payment-management-service/pom.xml
COPY module-5-orchestration/communication-management-service/pom.xml module-5-orchestration/communication-management-service/pom.xml
COPY module-5-orchestration/process-flow-service/pom.xml module-5-orchestration/process-flow-service/pom.xml

RUN mvn -pl module-1-customer/customer-management-service dependency:go-offline -B -q

COPY module-1-customer/customer-management-service/src module-1-customer/customer-management-service/src
RUN mvn -pl module-1-customer/customer-management-service package -DskipTests -B -q

# ── Stage 2: Runtime ─────────────────────────────────────────────────────────
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /workspace/module-1-customer/customer-management-service/target/*.jar app.jar
EXPOSE 8083
ENTRYPOINT ["java", "-jar", "app.jar"]
