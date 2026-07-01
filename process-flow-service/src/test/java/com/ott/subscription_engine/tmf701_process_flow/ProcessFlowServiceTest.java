**

```java
package com.ott.subscription_engine.tmf701_process_flow;

import com.ott.subscription_engine.tmf620_catalog.model.ProductOffering;
import com.ott.subscription_engine.tmf620_catalog.model.ProductOfferingPrice;
import com.ott.subscription_engine.tmf620_catalog.model.ProductOfferingRef;
import com.ott.subscription_engine.tmf620_catalog.model.ProductOfferingPriceRef;
import com.ott.subscription_engine.tmf620_catalog.model.Money;
import com.ott.subscription_engine.tmf622_ordering.model.ProductOrder;
import com.ott.subscription_engine.tmf629_customer.model.Customer;
import com.ott.subscription_engine.tmf637_inventory.model.Product;
import com.ott.subscription_engine.tmf676_payment.model.Payment;
import com.ott.subscription_engine.tmf678_billing.model.CustomerBill;
import com.ott.subscription_engine.tmf701_process_flow.model.ProcessFlow;
import com.ott.subscription_engine.tmf701_process_flow.model.ProcessFlowDTO;
import com.ott.subscription_engine.tmf701_process_flow.model.ProcessFlowListDTO;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/tmf-api/process-flow/v1")
public class ProcessFlowServiceTest {

    @Autowired
    private ProcessFlowService processFlowService;

    @MockBean
    private ProcessFlowRestHelper restHelper;

    @Autowired
    private ProcessFlowRepository processFlowRepository;

    private Customer createMockCustomer(String id, String status) {
        Customer customer = new Customer();
        customer.setId(id);
        customer.setName("Charlie Customer");
        customer.setStatus(status);
        return customer;
    }

    private ProductOffering createMockOffering(String id) {
        ProductOffering offering = new ProductOffering();
        offering.setId(id);
        offering.setName("Standard plan offering");
        offering.setLifecycleStatus("Active");

        ProductOfferingPriceRef priceRef = new ProductOfferingPriceRef();
        priceRef.setId("price-123");
        offering.setPriceRef(priceRef);

        return offering;
    }

    @GetMapping("/process-flows")
    public ResponseEntity<ProcessFlowListDTO> getProcessFlows(@RequestParam(value = "offset", required = false) Integer offset,
                                                               @RequestParam(value = "limit", required = false) Integer limit,
                                                               @RequestParam(value = "fields", required = false) String fields) {
        List<ProcessFlow> processFlows = process