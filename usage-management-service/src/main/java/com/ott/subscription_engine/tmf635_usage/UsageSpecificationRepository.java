package com.ott.subscription_engine.tmf635_usage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsageSpecificationRepository extends JpaRepository<UsageSpecificationEntity, String> {
}
