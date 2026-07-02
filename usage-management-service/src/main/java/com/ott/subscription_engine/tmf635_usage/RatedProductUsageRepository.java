package com.ott.subscription_engine.tmf635_usage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RatedProductUsageRepository extends JpaRepository<RatedProductUsageEntity, String> {
    // Gap 4.2 fix: query via JPA FK relationship (usage.id) instead of loose String usageId column
    List<RatedProductUsageEntity> findByUsage_Id(String usageId);
}
