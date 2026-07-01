package com.ott.subscription_engine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class UsageManagementServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(UsageManagementServiceApplication.class, args);
    }
}
