package com.subscriptionengine.productusage.infrastructure.persistence.adapter;

import com.subscriptionengine.productusage.domain.model.UsageConsumption;
import com.subscriptionengine.productusage.infrastructure.persistence.entity.UsageConsumptionJpaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Mapper(componentModel = "spring")
public interface UsageConsumptionPersistenceMapper {
    UsageConsumptionPersistenceMapper INSTANCE = Mappers.getMapper(UsageConsumptionPersistenceMapper.class);

    UsageConsumptionJpaEntity toEntity(UsageConsumption domain);
    UsageConsumption toDomain(UsageConsumptionJpaEntity entity);

    default LocalDateTime map(OffsetDateTime value) {
        if (value == null) {
            return null;
        }
        return value.toLocalDateTime();
    }

    default OffsetDateTime map(LocalDateTime value) {
        if (value == null) {
            return null;
        }
        return value.atOffset(ZoneOffset.UTC);
    }
}
