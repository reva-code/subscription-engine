package com.apex.payment.gateway.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestTemplate;

import com.apex.payment.gateway.gateway.PaymentGateway;

@Configuration
public class GatewayConfig {

    private final PaymentGatewayProperties properties;
    private final ApplicationContext applicationContext;

    public GatewayConfig(PaymentGatewayProperties properties, ApplicationContext applicationContext) {
        this.properties = properties;
        this.applicationContext = applicationContext;
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean
    @Primary
    public PaymentGateway activePaymentGateway() {
        String provider = properties.getProvider();
        if (provider == null || provider.trim().isEmpty()) {
            throw new IllegalArgumentException("Payment gateway provider is not configured. Configure 'payment.gateway.provider'.");
        }

        switch (provider.trim().toLowerCase()) {
            case "razorpay":
                return applicationContext.getBean("razorpayGatewayAdapter", PaymentGateway.class);
            case "mock":
                return applicationContext.getBean("mockPaymentGateway", PaymentGateway.class);
            default:
                throw new IllegalArgumentException("Unsupported payment gateway provider: " + provider);
        }
    }
}
