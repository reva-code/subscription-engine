package com.ott.subscription_engine.tmf635_usage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UsageRepository extends JpaRepository<UsageEntity, String> {
    List<UsageEntity> findByProductId(String productId);
    List<UsageEntity> findByCustomerId(String customerId);
    List<UsageEntity> findBySubscriptionId(String subscriptionId);
    long countByCustomerId(String customerId);
    List<String> findIdByCustomerId(String customerId);
}
