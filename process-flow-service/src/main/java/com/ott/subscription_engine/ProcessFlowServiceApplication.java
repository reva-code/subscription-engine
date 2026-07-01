package com.ott.subscription_engine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan(basePackages = {
        "com.ott.subscription_engine.tmf701_process_flow",
        "com.ott.subscription_engine.config",
        "com.ott.subscription_engine.common",
        "com.ott.subscription_engine.security"
}, excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX,
        pattern = "com\\.ott\\.subscription_engine\\.common\\..*Controller"))
@EntityScan(basePackages = {
        "com.ott.subscription_engine.tmf701_process_flow"
})
@EnableJpaRepositories(basePackages = {
        "com.ott.subscription_engine.tmf701_process_flow"
})
@EnableScheduling
public class ProcessFlowServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProcessFlowServiceApplication.class, args);
    }
}
