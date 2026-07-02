package com.subscriptionengine.productusage.api.controller;

import com.subscriptionengine.productusage.api.mapper.UsageConsumptionApiMapper;
import com.subscriptionengine.productusage.domain.model.UsageConsumption;
import com.subscriptionengine.productusage.domain.port.in.UsageConsumptionUseCase;
import org.openapitools.api.QueryUsageConsumptionApi;
import org.openapitools.model.QueryUsageConsumption;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tmf-api/productUsageManagement/v4")
public class QueryUsageConsumptionApiController implements QueryUsageConsumptionApi {

    private final UsageConsumptionUseCase useCase;
    private final UsageConsumptionApiMapper mapper;

    public QueryUsageConsumptionApiController(UsageConsumptionUseCase useCase, UsageConsumptionApiMapper mapper) {
        this.useCase = useCase;
        this.mapper = mapper;
    }

    @Override
    public ResponseEntity<List<QueryUsageConsumption>> listQueryUsageConsumption(String fields, Integer offset, Integer limit) {
        List<UsageConsumption> usages = useCase.getAllUsages();
        
        List<org.openapitools.model.UsageConsumption> mappedUsages = usages.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());

        QueryUsageConsumption report = new QueryUsageConsumption();
        report.setId(UUID.randomUUID().toString());
        report.setHref(URI.create("/queryUsageConsumption/" + report.getId()));
        report.setQueryUsageConsumptionDate(OffsetDateTime.now());
        report.setUsageConsumption(mappedUsages);

        return ResponseEntity.ok(Collections.singletonList(report));
    }
}
