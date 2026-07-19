package com.apex.payment.gateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "payment.gateway")
public class PaymentGatewayProperties {

    private String provider;
    private RazorpayProperties razorpay = new RazorpayProperties();

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public RazorpayProperties getRazorpay() {
        return razorpay;
    }

    public void setRazorpay(RazorpayProperties razorpay) {
        this.razorpay = razorpay;
    }

    public static class RazorpayProperties {
        private String keyId;
        private String keySecret;
        private String webhookSecret;

        public String getKeyId() {
            return keyId;
        }

        public void setKeyId(String keyId) {
            this.keyId = keyId;
        }

        public String getKeySecret() {
            return keySecret;
        }

        public void setKeySecret(String keySecret) {
            this.keySecret = keySecret;
        }

        public String getWebhookSecret() {
            return webhookSecret;
        }

        public void setWebhookSecret(String webhookSecret) {
            this.webhookSecret = webhookSecret;
        }
    }
}
