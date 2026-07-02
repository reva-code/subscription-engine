package com.jio.tmf622;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// @SpringBootApplication — one annotation that does three things:
//
//  1. @Configuration
//     This class itself can declare @Bean methods (we don't need any yet,
//     but Spring needs at least one configuration class to boot from).
//
//  2. @EnableAutoConfiguration
//     Spring Boot inspects the JARs on the classpath and wires up beans
//     automatically. Because we added spring-boot-starter-data-jpa,
//     it will auto-configure:
//       - A HikariCP connection pool using our datasource properties
//       - A Hibernate EntityManagerFactory pointed at those properties
//       - Spring Data JPA repositories (finds ProductOrderRepository)
//       - Schema management (ddl-auto=update creates tables on startup)
//
//  3. @ComponentScan (implicit, scans from this package downward)
//     Spring scans com.jio.tmf622 and every sub-package:
//       com.jio.tmf622.controller  → registers ProductOrderController
//       com.jio.tmf622.service     → registers ProductOrderService
//       com.jio.tmf622.repository  → registers ProductOrderRepository
//       com.jio.tmf622.entity      → Hibernate picks up @Entity classes
//     org.openapitools.configuration is also picked up because SpringDoc
//     auto-configuration handles EnumConverterConfiguration separately.
@SpringBootApplication
public class ProductOrderServiceApplication {

    // SpringApplication.run() bootstraps the whole application:
    //   1. Creates the Spring ApplicationContext (the bean container)
    //   2. Runs auto-configuration
    //   3. Starts the embedded Tomcat server on server.port (8080)
    //   4. Registers all controller routes
    //   5. Runs Hibernate schema validation/update against MariaDB
    public static void main(String[] args) {
        SpringApplication.run(ProductOrderServiceApplication.class, args);
    }
}
