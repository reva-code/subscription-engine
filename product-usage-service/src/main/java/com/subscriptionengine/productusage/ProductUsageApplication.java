package com.subscriptionengine.productusage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ProductUsageApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProductUsageApplication.class, args);
    }
}
