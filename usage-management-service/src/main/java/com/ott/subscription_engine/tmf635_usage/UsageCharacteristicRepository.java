package com.ott.subscription_engine.tmf635_usage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UsageCharacteristicRepository extends JpaRepository<UsageCharacteristicEntity, String> {
    List<UsageCharacteristicEntity> findByUsage_Id(String usageId);
    void deleteByUsage_Id(String usageId);
}
