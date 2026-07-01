package com.ott.subscription_engine.subscription;

import com.ott.subscription_engine.tmf635_usage.UsageEntity;
import com.ott.subscription_engine.tmf635_usage.UsageRepository;
import com.ott.subscription_engine.tmf635_usage.UsageService;
import com.ott.subscription_engine.tmf635_usage.UsageSpecificationEntity;
import com.ott.subscription_engine.tmf635_usage.UsageSpecificationRepository;
import com.ott.subscription_engine.tmf635_usage.model.Usage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = com.ott.subscription_engine.UsageManagementServiceApplication.class)
@ActiveProfiles("test")
@Transactional
public class UsageRatingTest {

    @Autowired
    private UsageService usageService;

    @Autowired
    private UsageRepository usageRepository;

    @Autowired
    private UsageSpecificationRepository usageSpecificationRepository;

    @Test
    public void testUsageRatingOverageAndWithinQuota() {
        // Create Usage Specification
        UsageSpecificationEntity spec = new UsageSpecificationEntity();
        spec.setName("Premium Stream Hours");
        spec.setDescription("Overage charged per hour");
        spec.setLifecycleStatus("Active");
        spec.setUnitOfMeasure("Hour");
        spec.setUnitPrice(BigDecimal.valueOf(15.00)); // ₹15/hr
        spec.setBaseQuota(10L); // 10 free hours
        spec = usageSpecificationRepository.save(spec);

        // 1. Within quota usage (8 hours)
        UsageEntity usage1 = new UsageEntity();
        usage1.setQuantity(8L);
        usage1.setUnit("Hour");
        usage1.setStatus("received");
        usage1.setProductId("prod-123");
        usage1.setUsageDate(LocalDateTime.now());
        usage1 = usageRepository.save(usage1);

        Usage rated1 = usageService.rateUsage(usage1.getId(), spec);
        UsageEntity ratedEntity1 = usageRepository.findById(usage1.getId()).orElseThrow();
        assertEquals(BigDecimal.ZERO.setScale(2), ratedEntity1.getRatedAmount().setScale(2));
        assertEquals("rated", ratedEntity1.getStatus());

        // 2. Over quota usage (15 hours, so 5 hours overage)
        UsageEntity usage2 = new UsageEntity();
        usage2.setQuantity(15L);
        usage2.setUnit("Hour");
        usage2.setStatus("received");
        usage2.setProductId("prod-123");
        usage2.setUsageDate(LocalDateTime.now());
        usage2 = usageRepository.save(usage2);

        Usage rated2 = usageService.rateUsage(usage2.getId(), spec);
        UsageEntity ratedEntity2 = usageRepository.findById(usage2.getId()).orElseThrow();
        // 15 - 10 = 5 overage hours * ₹15 = ₹75.00
        assertEquals(BigDecimal.valueOf(75.00).setScale(2), ratedEntity2.getRatedAmount().setScale(2));
        assertEquals("rated", ratedEntity2.getStatus());
    }
}
