**

```java
package com.ott.subscription_engine.tmf701_process_flow;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ott.subscription_engine.tmf620_catalog.model.ProductOffering;
import com.ott.subscription_engine.tmf620_catalog.model.ProductOfferingPrice;
import com.ott.subscription_engine.tmf622_ordering.model.ProductOrder;
import com.ott.subscription_engine.tmf622_ordering.model.ProductOrderCreate;
import com.ott.subscription_engine.tmf629_customer.model.Customer;
import com.ott.subscription_engine.tmf637_inventory.model.Product;
import com.ott.subscription_engine.tmf676_payment.model.Payment;
import com.ott.subscription_engine.tmf676_payment.model.PaymentCreate;
import com.ott.subscription_engine.tmf676_payment.model.RefundCreate;
import com.ott.subscription_engine.tmf678_billing.model.CustomerBill;
import com.ott.subscription_engine.tmf678_billing.model.CustomerBillUpdate;
import com.ott.subscription_engine.tmf681_communication.model.CommunicationMessageCreate;
import com.ott.subscription_engine.common.dto.SubscriptionDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/tmf-api/customerManagement/v4")
public class ProcessFlowRestHelper {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public ProcessFlowRestHelper(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/{customerId}")
    @PreAuthorize("hasRole('CUSTOMER_READ')")
    public ResponseEntity<Customer> getCustomer(@PathVariable @Valid @Pattern(regexp = "[a-zA-Z0-9]+") String customerId) {
        Customer customer = restTemplate.getForObject("http://localhost:8081/tmf-api/customerManagement/v4/customer/" + customerId, Customer.class);
        customer.setHref(ServletUriComponentsBuilder.fromCurrentRequest().build().toUriString());
        customer.setId(customerId);
        customer.setType(Customer.class.getSimpleName());
        return ResponseEntity.ok(customer);
    }

    @GetMapping("/productOffering/{offeringId}")
    @PreAuthorize("hasRole('CUSTOMER_READ')")
    public ResponseEntity<ProductOffering> getProductOffering(@PathVariable @Valid @Pattern(regexp = "[a-zA-Z0-9]+") String offeringId) {
        ProductOffering productOffering = restTemplate.getForObject("http://localhost:8081/tmf-api/productOfferingManagement/v4/productOffering/" + offeringId, ProductOffering.class);
        productOffering.setHref(ServletUriComponentsBuilder.fromCurrentRequest().build().toUriString());
        productOffering.setId(offeringId);