package com.subscriptionengine.productusage.api.controller;

import com.subscriptionengine.productusage.api.mapper.UsageConsumptionApiMapper;
import com.subscriptionengine.productusage.domain.model.UsageConsumption;
import com.subscriptionengine.productusage.domain.port.in.UsageConsumptionUseCase;
import org.openapitools.api.UsageConsumptionApi;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tmf-api/productUsageManagement/v4")
public class UsageConsumptionApiController implements UsageConsumptionApi {

    private final UsageConsumptionUseCase useCase;
    private final UsageConsumptionApiMapper mapper;

    public UsageConsumptionApiController(UsageConsumptionUseCase useCase, UsageConsumptionApiMapper mapper) {
        this.useCase = useCase;
        this.mapper = mapper;
    }

    @Override
    public ResponseEntity<org.openapitools.model.UsageConsumption> createUsageConsumption(org.openapitools.model.UsageConsumption usageConsumptionDto) {
        UsageConsumption domain = mapper.toDomain(usageConsumptionDto);
        UsageConsumption saved = useCase.createUsageConsumption(domain);
        return new ResponseEntity<>(mapper.toDto(saved), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<List<org.openapitools.model.UsageConsumption>> createUsageConsumptionBatch(List<org.openapitools.model.UsageConsumption> usageConsumptionDtoList) {
        List<UsageConsumption> domains = usageConsumptionDtoList.stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
        List<UsageConsumption> saved = useCase.createUsageConsumptionBatch(domains);
        List<org.openapitools.model.UsageConsumption> dtos = saved.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(dtos, HttpStatus.CREATED);
    }
}
