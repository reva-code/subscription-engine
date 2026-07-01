package com.ott.subscription_engine.tmf635_usage;

import com.ott.subscription_engine.tmf635_usage.model.UsageCreate;
import com.ott.subscription_engine.tmf635_usage.model.UsageCharacteristic;
import com.ott.subscription_engine.tmf635_usage.model.UsageStatusType;
import com.ott.subscription_engine.tmf635_usage.model.Usage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.OffsetDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Gap 7.1 fix: Integration tests now run against a REAL Testcontainers MariaDB container
 * instead of H2 in-memory (which was loaded via @ActiveProfiles("test")).
 *
 * Using @DynamicPropertySource, the MariaDB container's JDBC URL, username and password
 * are injected at runtime — overriding application.yaml datasource config.
 *
 * This catches real MariaDB dialect issues (strict mode, case sensitivity, JSON types)
 * that H2 in MODE=MySQL silently ignores.
 */
@Testcontainers
@SpringBootTest
class UsageIntegrationTest {

    @Container
    static MariaDBContainer<?> mariaDB = new MariaDBContainer<>("mariadb:10.11")
            .withDatabaseName("jio_usage_test")
            .withUsername("testuser")
            .withPassword("testpass");

    @DynamicPropertySource
    static void overrideDataSourceProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url",      mariaDB::getJdbcUrl);
        registry.add("spring.datasource.username", mariaDB::getUsername);
        registry.add("spring.datasource.password", mariaDB::getPassword);
        registry.add("spring.datasource.driver-class-name", () -> "org.mariadb.jdbc.Driver");
        registry.add("spring.jpa.hibernate.ddl-auto",       () -> "create-drop");
        registry.add("spring.jpa.properties.hibernate.dialect", () -> "org.hibernate.dialect.MariaDBDialect");
    }

    @Autowired UsageService usageService;
    @Autowired UsageRepository usageRepository;
    @Autowired RatedProductUsageRepository ratedProductUsageRepository;
    @Autowired UsageCharacteristicRepository usageCharacteristicRepository;

    @Test
    void createUsage_persistsToRealMariaDB() {
        UsageCreate dto = new UsageCreate();
        dto.setUsageDate(OffsetDateTime.now());
        dto.setStatus(UsageStatusType.RECEIVED);
        dto.setUsageCharacteristic(List.of(
                new UsageCharacteristic("quantity", "10"),
                new UsageCharacteristic("unit", "GB"),
                new UsageCharacteristic("customerId", "cust-001"),
                new UsageCharacteristic("subscriptionId", "sub-001"),
                // Gap 4.1 fix verification: non-standard characteristics are now also persisted
                new UsageCharacteristic("sessionId", "sess-xyz"),
                new UsageCharacteristic("ipAddress", "192.168.1.1")
        ));

        Usage saved = usageService.createUsage(dto);

        assertThat(saved.getId()).isNotNull();
        assertThat(usageRepository.findById(saved.getId())).isPresent();

        // Verify ALL 6 characteristics were persisted (not just quantity + unit)
        List<UsageCharacteristicEntity> persisted = usageCharacteristicRepository.findByUsage_Id(saved.getId());
        assertThat(persisted).hasSize(6);
        assertThat(persisted).anyMatch(c -> "sessionId".equals(c.getName()) && "sess-xyz".equals(c.getValue()));
        assertThat(persisted).anyMatch(c -> "ipAddress".equals(c.getName()) && "192.168.1.1".equals(c.getValue()));
    }

    @Test
    void listUsage_queryByCustomerId_returnsCorrectRecords() {
        UsageCreate dto = new UsageCreate();
        dto.setUsageDate(OffsetDateTime.now());
        dto.setStatus(UsageStatusType.RECEIVED);
        dto.setUsageCharacteristic(List.of(
                new UsageCharacteristic("quantity", "5"),
                new UsageCharacteristic("unit", "hours"),
                new UsageCharacteristic("customerId", "cust-filter-test")
        ));
        usageService.createUsage(dto);

        List<Usage> results = usageService.getUsagesByCustomer("cust-filter-test");
        assertThat(results).isNotEmpty();
        assertThat(results).allSatisfy(u ->
                assertThat(usageRepository.findById(u.getId()).get().getCustomerId())
                        .isEqualTo("cust-filter-test"));
    }

    @Test
    void listUsage_queryBySubscriptionId_returnsCorrectRecords() {
        UsageCreate dto = new UsageCreate();
        dto.setUsageDate(OffsetDateTime.now());
        dto.setStatus(UsageStatusType.RECEIVED);
        dto.setUsageCharacteristic(List.of(
                new UsageCharacteristic("quantity", "3"),
                new UsageCharacteristic("unit", "MB"),
                new UsageCharacteristic("subscriptionId", "sub-filter-test")
        ));
        usageService.createUsage(dto);

        List<Usage> results = usageService.getUsagesBySubscription("sub-filter-test");
        assertThat(results).isNotEmpty();
    }

    @Autowired
    UsageSpecificationRepository usageSpecificationRepository;

    @Test
    void rateUsage_createsRatedProductUsageEntityWithJpaFk() {
        UsageSpecificationEntity spec = new UsageSpecificationEntity();
        spec.setName("DataSpec");
        spec.setDescription("Data usage spec");
        spec.setLifecycleStatus("Active");
        spec.setUnitOfMeasure("GB");
        spec.setUnitPrice(new java.math.BigDecimal("1.00"));
        spec.setBaseQuota(5L);
        UsageSpecificationEntity savedSpec = usageSpecificationRepository.save(spec);

        UsageCreate dto = new UsageCreate();
        dto.setUsageDate(OffsetDateTime.now());
        dto.setStatus(UsageStatusType.RECEIVED);
        dto.setUsageCharacteristic(List.of(new UsageCharacteristic("quantity", "20")));
        Usage created = usageService.createUsage(dto);

        Usage rated = usageService.rateUsageById(created.getId(), savedSpec.getId());

        assertThat(rated.getStatus()).isEqualTo(UsageStatusType.RATED);

        // Gap 4.2 fix verification: query via JPA FK relationship
        List<RatedProductUsageEntity> ratedRecords = ratedProductUsageRepository.findByUsage_Id(rated.getId());
        assertThat(ratedRecords).hasSize(1);
        assertThat(ratedRecords.get(0).getTaxExcludedRatingAmount()).isPositive();
        assertThat(ratedRecords.get(0).getCurrencyCode()).isEqualTo("INR");
        // Verify JPA FK is actually set (not just the ID)
        assertThat(ratedRecords.get(0).getUsage()).isNotNull();
        assertThat(ratedRecords.get(0).getUsage().getId()).isEqualTo(rated.getId());
    }
}
