package com.subscriptionengine.productusage.api.mapper;

import com.subscriptionengine.productusage.domain.model.UsageConsumption;
import com.subscriptionengine.productusage.domain.model.UsageConsumptionProductRef;
import com.subscriptionengine.productusage.domain.model.RelatedParty;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UsageConsumptionApiMapper {
    UsageConsumption toDomain(org.openapitools.model.UsageConsumption dto);
    org.openapitools.model.UsageConsumption toDto(UsageConsumption domain);

    UsageConsumptionProductRef toDomain(org.openapitools.model.UsageConsumptionProductRef dto);
    org.openapitools.model.UsageConsumptionProductRef toDto(UsageConsumptionProductRef domain);

    RelatedParty toDomain(org.openapitools.model.RelatedParty dto);
    org.openapitools.model.RelatedParty toDto(RelatedParty domain);

    default String map(java.net.URI value) {
        return value == null ? null : value.toString();
    }

    default java.net.URI map(String value) {
        return value == null ? null : java.net.URI.create(value);
    }
}
