package com.ott.subscription_engine.tmf635_usage;

import com.ott.subscription_engine.tmf635_usage.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsageServiceTest {

    @Mock UsageRepository usageRepository;
    @Mock UsageSpecificationRepository usageSpecificationRepository;
    @Mock RatedProductUsageRepository ratedProductUsageRepository;
    @InjectMocks UsageService service;

    private UsageEntity entity;

    @BeforeEach
    void setup() {
        entity = new UsageEntity();
        entity.setId("u1");
        entity.setHref("/tmf-api/usageManagement/v4/usage/u1");
        entity.setProductId("p1");
        entity.setUsageDate(LocalDateTime.of(2026, 6, 10, 12, 0));
        entity.setQuantity(5L);
        entity.setUnit("hours");
        entity.setStatus("received");
        entity.setRatedAmount(BigDecimal.ZERO);
    }

    @Test
    void toDto_mapsUsageDateAndCharacteristics() {
        Usage dto = service.toDto(entity);

        assertThat(dto.getId()).isEqualTo("u1");
        assertThat(dto.getUsageDate()).isNotNull();
        assertThat(dto.getStatus()).isEqualTo(UsageStatusType.RECEIVED);
        assertThat(dto.getUsageCharacteristic()).hasSize(2);

        UsageCharacteristic qtyChar = dto.getUsageCharacteristic().stream()
                .filter(c -> "quantity".equals(c.getName())).findFirst().orElseThrow();
        assertThat(qtyChar.getValue()).isEqualTo(5L);

        UsageCharacteristic unitChar = dto.getUsageCharacteristic().stream()
                .filter(c -> "unit".equals(c.getName())).findFirst().orElseThrow();
        assertThat(unitChar.getValue()).isEqualTo("hours");
    }

    @Test
    void toDto_nullEntity_returnsNull() {
        assertThat(service.toDto((UsageEntity) null)).isNull();
    }

    @Test
    void toDto_nullQuantity_noCharacteristicsAdded() {
        entity.setQuantity(null);
        entity.setUnit(null);
        Usage dto = service.toDto(entity);
        assertThat(dto.getUsageCharacteristic()).isNullOrEmpty();
    }

    @Test
    void createUsage_readsQuantityFromCharacteristic() {
        UsageCreate dto = new UsageCreate();
        dto.setUsageDate(OffsetDateTime.now());
        dto.setStatus(UsageStatusType.RECEIVED);
        dto.setUsageCharacteristic(List.of(
                new UsageCharacteristic("quantity", "3"),
                new UsageCharacteristic("unit", "GB")
        ));

        UsageEntity saved = new UsageEntity();
        saved.setId("u2");
        saved.setQuantity(3L);
        saved.setUnit("GB");
        saved.setStatus("received");

        when(usageRepository.save(any())).thenReturn(saved);

        Usage result = service.createUsage(dto);

        assertThat(result).isNotNull();
        verify(usageRepository, times(2)).save(any());
    }

    @Test
    void rateUsage_multipliesQuantityByUnitPrice() {
        entity.setQuantity(10L);
        UsageSpecificationEntity spec = new UsageSpecificationEntity();
        spec.setId("s1");
        spec.setUnitPrice(new BigDecimal("2.50"));

        when(usageRepository.findById("u1")).thenReturn(Optional.of(entity));
        when(usageRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Usage rated = service.rateUsage("u1", spec);

        assertThat(rated.getStatus()).isEqualTo(UsageStatusType.RATED);
        assertThat(entity.getRatedAmount()).isEqualByComparingTo("25.00");
    }

    @Test
    void rateUsage_alreadyRated_noChange() {
        entity.setStatus("rated");
        UsageSpecificationEntity spec = new UsageSpecificationEntity();
        spec.setUnitPrice(BigDecimal.TEN);

        when(usageRepository.findById("u1")).thenReturn(Optional.of(entity));

        Usage result = service.rateUsage("u1", spec);

        assertThat(entity.getRatedAmount()).isEqualByComparingTo(BigDecimal.ZERO);
        verify(usageRepository, never()).save(any());
    }
}
