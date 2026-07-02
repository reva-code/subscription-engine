package com.jio.customer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * TMF629 Customer Management Service
 * Port: 8083  |  DB: jio_customer
 *
 * Endpoints:
 *   /tmf-api/customerManagement/v4/customer
 *   /tmf-api/customerManagement/v4/customerAccount
 *   /swagger-ui/index.html
 */
@SpringBootApplication
public class CustomerManagementApplication {
    public static void main(String[] args) {
        SpringApplication.run(CustomerManagementApplication.class, args);
    }
}
