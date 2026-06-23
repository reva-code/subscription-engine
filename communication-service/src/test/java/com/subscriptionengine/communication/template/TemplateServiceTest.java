package com.subscriptionengine.communication.template;

import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

public class TemplateServiceTest {

    private final TemplateService templateService = new TemplateService();

    @Test
    public void testEmailTemplateRendering() {
        Map<String, String> vars = new HashMap<>();
        vars.put("customerName", "John Doe");
        vars.put("amount", "$99.99");
        vars.put("subscriptionId", "SUB-1234");
        vars.put("paymentId", "PAY-999");
        vars.put("date", "2026-06-17");

        String renderedHtml = templateService.renderEmail("PAYMENT_SUCCESS", vars);

        assertTrue(renderedHtml.contains("John Doe"));
        assertTrue(renderedHtml.contains("$99.99"));
        assertTrue(renderedHtml.contains("SUB-1234"));
        assertTrue(renderedHtml.contains("PAY-999"));
        assertTrue(renderedHtml.contains("2026-06-17"));
        assertTrue(renderedHtml.contains("Payment Successful"));
    }

    @Test
    public void testSmsTemplateRendering() {
        Map<String, String> vars = new HashMap<>();
        vars.put("customerName", "Alice");
        vars.put("amount", "$49.99");
        vars.put("subscriptionId", "SUB-5678");
        vars.put("date", "2026-06-18");

        String renderedSms = templateService.renderSms("SUBSCRIPTION_RENEWAL", vars);

        assertTrue(renderedSms.contains("Alice"));
        assertTrue(renderedSms.contains("$49.99"));
        assertTrue(renderedSms.contains("SUB-5678"));
        assertTrue(renderedSms.contains("2026-06-18"));
    }

    @Test
    public void testGetEmailSubject() {
        assertEquals("Payment Successful - Subscription Engine", templateService.getEmailSubject("PAYMENT_SUCCESS"));
        assertEquals("Action Required: Payment Failed", templateService.getEmailSubject("PAYMENT_FAILURE"));
        assertEquals("Notification from Subscription Engine", templateService.getEmailSubject("INVALID_TEMPLATE"));
    }
}
