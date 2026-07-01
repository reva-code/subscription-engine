package com.ott.subscription_engine.tmf635_usage;

import com.ott.subscription_engine.tmf635_usage.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.net.URI;
import java.time.ZoneOffset;
import org.springframework.data.domain.PageRequest;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UsageService implements com.ott.subscription_engine.tmf635_usage.service.UsageServicePort {

    private final UsageRepository usageRepository;
    private final UsageSpecificationRepository usageSpecificationRepository;
    private final RatedProductUsageRepository ratedProductUsageRepository;
    private final UsageCharacteristicRepository usageCharacteristicRepository;

    private static final String BASE_USAGE_PATH = "/tmf-api/usageManagement/v4/usage";
    private static final String BASE_SPEC_PATH = "/tmf-api/usageManagement/v4/usageSpecification";

    private java.time.OffsetDateTime toOffsetDateTime(java.time.LocalDateTime ldt) {
        if (ldt == null) return null;
        // RFC 3339 — always UTC, never system-default
        return ldt.atOffset(ZoneOffset.UTC);
    }

    private java.time.LocalDateTime toLocalDateTime(java.time.OffsetDateTime odt) {
        if (odt == null) return null;
        return odt.withOffsetSameInstant(ZoneOffset.UTC).toLocalDateTime();
    }

    // ── Usage Mapping ────────────────────────────────────────────────────

    public Usage toDto(UsageEntity entity) {
        if (entity == null) return null;
        Usage dto = new Usage();
        dto.setId(entity.getId());
        if (entity.getHref() != null) {
            try {
                dto.setHref(URI.create(entity.getHref()));
            } catch (Exception e) {}
        }
        if (entity.getStatus() != null) {
            try {
                dto.setStatus(UsageStatusType.fromValue(entity.getStatus().toLowerCase()));
            } catch (Exception e) {}
        }
        dto.setUsageDate(toOffsetDateTime(entity.getUsageDate()));
        dto.setDescription(entity.getDescription());
        dto.setUsageType(entity.getUsageType());

        // Gap 4.1 fix: map ALL persisted characteristics back to DTO
        if (entity.getCharacteristics() != null) {
            entity.getCharacteristics().forEach(uc ->
                dto.addUsageCharacteristicItem(new UsageCharacteristic(uc.getName(), uc.getValue()))
            );
        }

        dto.setAtType(entity.getAtType());
        dto.setAtBaseType(entity.getAtBaseType());
        if (entity.getAtSchemaLocation() != null) {
            try {
                dto.setAtSchemaLocation(URI.create(entity.getAtSchemaLocation()));
            } catch (Exception e) {}
        }
        return dto;
    }

    @Transactional
    public Usage createUsage(UsageCreate createDto) {
        UsageEntity entity = new UsageEntity();
        entity.setProductId("product_ref_" + System.currentTimeMillis());
        if (createDto.getUsageDate() != null) {
            entity.setUsageDate(toLocalDateTime(createDto.getUsageDate()));
        }
        entity.setDescription(createDto.getDescription());
        entity.setUsageType(createDto.getUsageType());
        entity.setStatus(createDto.getStatus() != null ? createDto.getStatus().getValue() : "received");

        // Extract well-known fields for indexed columns; also store all characteristics generically
        if (createDto.getUsageCharacteristic() != null) {
            for (UsageCharacteristic uc : createDto.getUsageCharacteristic()) {
                if (uc.getName() == null) continue;
                switch (uc.getName().toLowerCase()) {
                    case "customerid"     -> entity.setCustomerId(uc.getValue() != null ? uc.getValue().toString() : null);
                    case "subscriptionid" -> entity.setSubscriptionId(uc.getValue() != null ? uc.getValue().toString() : null);
                    default -> {} // all characteristics stored in child table below
                }
            }
        }

        // Persist entity first to get an ID, then attach characteristics
        UsageEntity saved = usageRepository.save(entity);
        saved.setHref(BASE_USAGE_PATH + "/" + saved.getId());
        saved = usageRepository.save(saved);

        // Gap 4.1 fix: persist ALL characteristics to usage_characteristic table
        if (createDto.getUsageCharacteristic() != null) {
            final UsageEntity finalSaved = saved;
            createDto.getUsageCharacteristic().stream()
                    .filter(uc -> uc.getName() != null)
                    .map(uc -> new UsageCharacteristicEntity(
                            finalSaved,
                            uc.getName(),
                            uc.getValue() != null ? uc.getValue().toString() : null))
                    .forEach(usageCharacteristicRepository::save);
        }

        return toDto(usageRepository.findById(saved.getId()).orElse(saved));
    }

    public Optional<Usage> getUsageById(String id) {
        return usageRepository.findById(id).map(this::toDto);
    }

    public List<Usage> getAllUsages() {
        return usageRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    /** Returns a paginated slice. offset and limit are both optional (null = no pagination). */
    public List<Usage> getAllUsagesPaginated(Integer offset, Integer limit) {
        if (limit != null && limit > 0) {
            int page = (offset != null && offset > 0) ? offset / limit : 0;
            return usageRepository.findAll(PageRequest.of(page, limit))
                    .stream().map(this::toDto).collect(Collectors.toList());
        }
        return getAllUsages();
    }

    public List<Usage> getUsagesByCustomer(String customerId) {
        return usageRepository.findByCustomerId(customerId).stream().map(this::toDto).collect(Collectors.toList());
    }

    public List<Usage> getUsagesBySubscription(String subscriptionId) {
        return usageRepository.findBySubscriptionId(subscriptionId).stream().map(this::toDto).collect(Collectors.toList());
    }

    @Transactional
    public Optional<Usage> patchUsage(String id, UsageUpdate dto) {
        return usageRepository.findById(id).map(entity -> {
            if (dto.getStatus() != null) entity.setStatus(dto.getStatus().getValue());
            if (dto.getUsageDate() != null) entity.setUsageDate(toLocalDateTime(dto.getUsageDate()));
            if (dto.getDescription() != null) entity.setDescription(dto.getDescription());
            if (dto.getUsageType() != null) entity.setUsageType(dto.getUsageType());
            if (dto.getUsageCharacteristic() != null) {
                // Gap 4.1: update characteristics in the child entity table
                usageCharacteristicRepository.deleteByUsage_Id(entity.getId());
                dto.getUsageCharacteristic().stream()
                        .filter(uc -> uc.getName() != null)
                        .map(uc -> new UsageCharacteristicEntity(
                                entity,
                                uc.getName(),
                                uc.getValue() != null ? uc.getValue().toString() : null))
                        .forEach(usageCharacteristicRepository::save);
            }
            return toDto(usageRepository.save(entity));
        });
    }

    @Transactional
    public boolean deleteUsage(String id) {
        if (usageRepository.existsById(id)) { usageRepository.deleteById(id); return true; }
        return false;
    }

    // ── Usage Specification Mapping ──────────────────────────────────────

    public UsageSpecification toDto(UsageSpecificationEntity entity) {
        if (entity == null) return null;
        UsageSpecification dto = new UsageSpecification();
        dto.setId(entity.getId());
        if (entity.getHref() != null) {
            try {
                dto.setHref(URI.create(entity.getHref()));
            } catch (Exception e) {}
        }
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setLifecycleStatus(entity.getLifecycleStatus());
        
        dto.setAtType(entity.getAtType());
        dto.setAtBaseType(entity.getAtBaseType());
        if (entity.getAtSchemaLocation() != null) {
            try {
                dto.setAtSchemaLocation(URI.create(entity.getAtSchemaLocation()));
            } catch (Exception e) {}
        }
        return dto;
    }

    @Transactional
    public UsageSpecification createUsageSpec(UsageSpecificationCreate createDto) {
        UsageSpecificationEntity entity = new UsageSpecificationEntity();
        entity.setName(createDto.getName());
        entity.setDescription(createDto.getDescription());
        entity.setLifecycleStatus(createDto.getLifecycleStatus());
        entity.setUnitOfMeasure("Hour");
        entity.setUnitPrice(BigDecimal.valueOf(10.00)); // default rating price
        entity.setBaseQuota(10L); // default quota of 10 free units

        UsageSpecificationEntity saved = usageSpecificationRepository.save(entity);
        saved.setHref(BASE_SPEC_PATH + "/" + saved.getId());
        return toDto(usageSpecificationRepository.save(saved));
    }

    public Optional<UsageSpecification> getUsageSpecById(String id) {
        return usageSpecificationRepository.findById(id).map(this::toDto);
    }

    public List<UsageSpecification> getAllUsageSpecs() {
        return usageSpecificationRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @Transactional
    public Optional<UsageSpecification> patchUsageSpec(String id, UsageSpecificationUpdate dto) {
        return usageSpecificationRepository.findById(id).map(entity -> {
            if (dto.getName() != null) entity.setName(dto.getName());
            if (dto.getDescription() != null) entity.setDescription(dto.getDescription());
            if (dto.getLifecycleStatus() != null) entity.setLifecycleStatus(dto.getLifecycleStatus());
            return toDto(usageSpecificationRepository.save(entity));
        });
    }

    @Transactional
    public boolean deleteUsageSpec(String id) {
        if (usageSpecificationRepository.existsById(id)) { usageSpecificationRepository.deleteById(id); return true; }
        return false;
    }

    // ── Rating Logic ─────────────────────────────────────────────────────

    @Transactional
    public Usage rateUsage(String usageId, UsageSpecificationEntity spec) {
        return usageRepository.findById(usageId).map(usage -> {
            if ("received".equalsIgnoreCase(usage.getStatus())) {
                // Gap 4.1 fix: read quantity from the characteristics collection
                Long quantity = usage.getCharacteristics().stream()
                        .filter(c -> "quantity".equalsIgnoreCase(c.getName()) && c.getValue() != null)
                        .mapToLong(c -> {
                            try { return Long.parseLong(c.getValue()); } catch (Exception e) { return 0L; }
                        })
                        .sum();
                Long quota = spec.getBaseQuota() != null ? spec.getBaseQuota() : 0L;

                BigDecimal charge = BigDecimal.ZERO;
                if (quantity > quota) {
                    long overage = quantity - quota;
                    charge = spec.getUnitPrice().multiply(BigDecimal.valueOf(overage));
                }
                usage.setRatedAmount(charge);
                usage.setStatus("rated");

                // Gap 4.2 fix: set the JPA relationship object, not just the String ID
                RatedProductUsageEntity rated = new RatedProductUsageEntity();
                rated.setUsage(usage);
                rated.setTaxExcludedRatingAmount(charge);
                rated.setTaxRate(new BigDecimal("18.00"));
                rated.setTaxIncludedRatingAmount(charge.multiply(new BigDecimal("1.18")).setScale(4, java.math.RoundingMode.HALF_UP));
                rated.setCurrencyCode("INR");
                rated.setOfferTariffType("StandardTariff");
                rated.setUsagePriceType("usage");
                ratedProductUsageRepository.save(rated);

                log.info("Usage {} rated at {} (quantity={}, quota={}, price={}) using spec {}",
                        usageId, charge, quantity, quota, spec.getUnitPrice(), spec.getId());
                return toDto(usageRepository.save(usage));
            }
            return toDto(usage);
        }).orElseThrow(() -> new IllegalArgumentException("Usage not found: " + usageId));
    }

    @Transactional
    public Usage rateUsageById(String usageId, String specId) {
        UsageSpecificationEntity spec = usageSpecificationRepository.findById(specId)
                .orElseThrow(() -> new IllegalArgumentException("Usage spec not found: " + specId));
        return rateUsage(usageId, spec);
    }

    @Override
    public long countUsageByCustomer(String customerId) {
        return usageRepository.countByCustomerId(customerId);
    }

    @Override
    public java.util.List<String> getUsageIdsByCustomer(String customerId) {
        return usageRepository.findByCustomerId(customerId).stream()
                .map(UsageEntity::getId).collect(Collectors.toList());
    }
}
