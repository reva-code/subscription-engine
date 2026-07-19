package com.apex.payment.gateway.gateway;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import org.springframework.web.client.RestTemplate;

import com.apex.payment.gateway.config.PaymentGatewayProperties;
import com.apex.payment.gateway.dto.GatewayStatus;
import com.apex.payment.gateway.dto.PaymentResponse;
import com.apex.payment.gateway.exception.PaymentCaptureException;
import com.apex.payment.gateway.mapper.GatewayErrorMapper;

public class RazorpayGatewayAdapterTest {

    private RestTemplate restTemplate;
    private MockRestServiceServer mockServer;
    private RazorpayGatewayAdapter adapter;

    @BeforeEach
    public void setup() {
        restTemplate = new RestTemplate();
        mockServer = MockRestServiceServer.createServer(restTemplate);

        PaymentGatewayProperties properties = new PaymentGatewayProperties();
        properties.setProvider("razorpay");
        PaymentGatewayProperties.RazorpayProperties razorpay = new PaymentGatewayProperties.RazorpayProperties();
        razorpay.setKeyId("rzp_test_keyid");
        razorpay.setKeySecret("rzp_test_secret");
        razorpay.setWebhookSecret("test_secret");
        properties.setRazorpay(razorpay);

        adapter = new RazorpayGatewayAdapter(properties, new GatewayErrorMapper(), restTemplate);
    }

    @Test
    public void captureUsesRemotePaymentDetails() {
        String transactionId = "pay_test_123";

        mockServer.expect(requestTo("https://api.razorpay.com/v1/payments/" + transactionId))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess("{" +
                        "\"id\": \"pay_test_123\"," +
                        "\"status\": \"authorized\"," +
                        "\"amount\": 10000," +
                        "\"currency\": \"INR\"}" , MediaType.APPLICATION_JSON));

        mockServer.expect(requestTo("https://api.razorpay.com/v1/payments/" + transactionId + "/capture"))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess("{" +
                        "\"id\": \"refund_test_123\"," +
                        "\"status\": \"captured\"}", MediaType.APPLICATION_JSON));

        PaymentResponse response = adapter.capture(transactionId);

        assertNotNull(response);
        assertEquals(transactionId, response.getTransactionId());
        assertEquals(GatewayStatus.CAPTURED, response.getStatus());
        assertEquals(new BigDecimal("100.00"), response.getAmount());
        assertEquals("INR", response.getCurrency());
        mockServer.verify();
    }

    @Test
    public void captureThrowsWhenTransactionNotCapturable() {
        String transactionId = "pay_test_456";

        mockServer.expect(requestTo("https://api.razorpay.com/v1/payments/" + transactionId))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess("{" +
                        "\"id\": \"pay_test_456\"," +
                        "\"status\": \"captured\"," +
                        "\"amount\": 10000," +
                        "\"currency\": \"INR\"}", MediaType.APPLICATION_JSON));

        assertThrows(PaymentCaptureException.class, () -> adapter.capture(transactionId));
        mockServer.verify();
    }
}
