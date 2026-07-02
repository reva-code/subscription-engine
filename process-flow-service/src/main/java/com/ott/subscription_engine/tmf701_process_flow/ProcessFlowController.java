**

```java
package com.ott.subscription_engine.tmf701_process_flow;

import com.ott.subscription_engine.common.TmfFilteringUtil;
import com.ott.subscription_engine.tmf701_process_flow.api.ProcessFlowApi;
import com.ott.subscription_engine.tmf701_process_flow.api.TaskFlowApi;
import com.ott.subscription_engine.tmf701_process_flow.model.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.NativeWebRequest;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/tmf-api/process-flow-management/v4")
@RequiredArgsConstructor
@Tag(name = "TMF701 - Process Flow Management", description = "E2E subscription orchestration engine per TM Forum TMF701")
public class ProcessFlowController implements ProcessFlowApi, TaskFlowApi {

    private final ProcessFlowService processFlowService;

    @Override
    public Optional<NativeWebRequest> getRequest() { return Optional.empty(); }

    private String getCharValue(ProcessFlowCreate flow, String charName) {
        if (flow.getCharacteristic() != null) {
            for (var c : flow.getCharacteristic()) {
                if (charName.equalsIgnoreCase(c.getName())) {
                    return String.valueOf(c.getValue());
                }
            }
        }
        return null;
    }

    @PreAuthorize("hasRole('PROCESS_FLOW')")
    @PostMapping
    public ResponseEntity<ProcessFlowResponse> createProcessFlow(@Valid @RequestBody ProcessFlowCreate processFlow) {
        String customerId = getCharValue(processFlow, "customerId");
        String billingAccountId = getCharValue(processFlow, "billingAccountId");
        String offeringId = getCharValue(processFlow, "offeringId");
        String paymentMethodToken = getCharValue(processFlow, "paymentMethodToken");

        if (customerId == null || billingAccountId == null || offeringId == null) {
            throw new IllegalArgumentException("Missing required process parameters in characteristics (customerId, billingAccountId, offeringId).");
        }

        ProcessFlow result = processFlowService.runOrchestratedCheckout(customerId, billingAccountId, offeringId, paymentMethodToken);
        URI location = URI.create("/tmf-api/process-flow-management/v4/process-flow/" + result.getId());
        ProcessFlowResponse response = new ProcessFlowResponse(result.getId(), location, ProcessFlowResponse.Type.PROCESS_FLOW, "ProcessFlow");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PreAuthorize("hasRole('PROCESS_FLOW')")
    @GetMapping
    public ResponseEntity<List<ProcessFlowResponse>> listProcessFlow(
            @RequestParam(value = "offset", required = false) Integer offset,
            @RequestParam(value = "limit", required = false) Integer limit,
            @RequestParam(value = "fields", required = false) String fields,
            @RequestParam(value = "filter", required = false) String filter) {
        List<ProcessFlow> processFlows = processFlowService.listProcessFlows(offset, limit, fields, filter);
        List<ProcessFlowResponse> responses =