package com.subscriptionengine.productusage.infrastructure.persistence.repository;

import com.subscriptionengine.productusage.infrastructure.persistence.entity.UsageConsumptionJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface UsageConsumptionRepository extends JpaRepository<UsageConsumptionJpaEntity, String>, JpaSpecificationExecutor<UsageConsumptionJpaEntity> {

    @Query(value = "SELECT * FROM usage_consumption u WHERE u.product_ref LIKE %:productId% AND u.creation_date BETWEEN :startDate AND :endDate", nativeQuery = true)
    List<UsageConsumptionJpaEntity> findTotalUsageByProductIdAndBillingPeriod(
            @Param("productId") String productId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query(value = "SELECT * FROM usage_consumption u WHERE u.product_ref LIKE %:productId%", nativeQuery = true)
    List<UsageConsumptionJpaEntity> findTotalUsageByProductId(@Param("productId") String productId);

    @Query(value = "SELECT * FROM usage_consumption u WHERE u.related_party LIKE %:customerId%", nativeQuery = true)
    List<UsageConsumptionJpaEntity> findTotalUsageByCustomerId(@Param("customerId") String customerId);

    @Query(value = "SELECT * FROM usage_consumption u WHERE u.name = :usageType", nativeQuery = true)
    List<UsageConsumptionJpaEntity> findTotalUsageByUsageType(@Param("usageType") String usageType);

    @Query(value = "SELECT u.related_party, COUNT(u.id) as usageCount FROM usage_consumption u GROUP BY u.related_party ORDER BY usageCount DESC LIMIT 10", nativeQuery = true)
    List<Object[]> findTopConsumers();
}
