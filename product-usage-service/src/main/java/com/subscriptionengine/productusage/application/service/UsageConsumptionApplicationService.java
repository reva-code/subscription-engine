package com.subscriptionengine.productusage.application.service;

import com.subscriptionengine.productusage.domain.model.UsageConsumption;
import com.subscriptionengine.productusage.domain.port.in.UsageConsumptionUseCase;
import com.subscriptionengine.productusage.domain.port.in.UsageValidationService;
import com.subscriptionengine.productusage.domain.port.out.UsageConsumptionRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UsageConsumptionApplicationService implements UsageConsumptionUseCase {

    private final UsageConsumptionRepositoryPort repositoryPort;

    private final UsageValidationService validationService;
    private final UsageConsumptionStateMachine stateMachine;

    public UsageConsumptionApplicationService(UsageConsumptionRepositoryPort repositoryPort, UsageValidationService validationService, UsageConsumptionStateMachine stateMachine) {
        this.repositoryPort = repositoryPort;
        this.validationService = validationService;
        this.stateMachine = stateMachine;
    }

    @Override
    public UsageConsumption createUsageConsumption(UsageConsumption usageConsumption) {
        validationService.validateUsage(usageConsumption);
        usageConsumption.setId(UUID.randomUUID().toString());
        usageConsumption.setHref("/productUsageManagement/v4/usageConsumption/" + usageConsumption.getId());
        if (usageConsumption.getCreationDate() == null) {
            usageConsumption.setCreationDate(OffsetDateTime.now());
        }
        if (usageConsumption.getStatus() == null) {
            usageConsumption.setStatus("RECEIVED");
        }
        stateMachine.process(usageConsumption);
        return repositoryPort.save(usageConsumption);
    }

    @Override
    public List<UsageConsumption> createUsageConsumptionBatch(List<UsageConsumption> usageConsumptions) {
        List<UsageConsumption> toSave = usageConsumptions.stream().map(usage -> {
            validationService.validateUsage(usage);
            usage.setId(UUID.randomUUID().toString());
            usage.setHref("/productUsageManagement/v4/usageConsumption/" + usage.getId());
            if (usage.getCreationDate() == null) usage.setCreationDate(OffsetDateTime.now());
            if (usage.getStatus() == null) usage.setStatus("RECEIVED");
            stateMachine.process(usage);
            return usage;
        }).collect(Collectors.toList());
        return repositoryPort.saveAll(toSave);
    }

    @Override
    public List<UsageConsumption> getAllUsages() {
        return repositoryPort.findAll();
    }
}
