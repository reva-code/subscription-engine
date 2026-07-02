package com.subscriptionengine.productusage.infrastructure.persistence.adapter;

import com.subscriptionengine.productusage.domain.model.UsageConsumption;
import com.subscriptionengine.productusage.domain.port.out.UsageConsumptionRepositoryPort;
import com.subscriptionengine.productusage.infrastructure.persistence.entity.UsageConsumptionJpaEntity;
import com.subscriptionengine.productusage.infrastructure.persistence.repository.UsageConsumptionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class UsageConsumptionPersistenceAdapter implements UsageConsumptionRepositoryPort {

    private final UsageConsumptionRepository repository;
    private final UsageConsumptionPersistenceMapper mapper;

    public UsageConsumptionPersistenceAdapter(UsageConsumptionRepository repository, UsageConsumptionPersistenceMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public UsageConsumption save(UsageConsumption usageConsumption) {
        UsageConsumptionJpaEntity entity = mapper.toEntity(usageConsumption);
        return mapper.toDomain(repository.save(entity));
    }

    @Override
    public List<UsageConsumption> saveAll(List<UsageConsumption> usageConsumptions) {
        List<UsageConsumptionJpaEntity> entities = usageConsumptions.stream()
                .map(mapper::toEntity)
                .collect(Collectors.toList());
        return repository.saveAll(entities).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<UsageConsumption> findById(String id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Page<UsageConsumption> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toDomain);
    }

    @Override
    public List<UsageConsumption> findTotalUsageByProductId(String productId) {
        return repository.findTotalUsageByProductId(productId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<UsageConsumption> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
}
