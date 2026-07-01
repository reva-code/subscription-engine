package com.ott.subscription_engine.tmf701_process_flow;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ProcessFlowRepository extends JpaRepository<ProcessFlowEntity, String> {
    Optional<ProcessFlowEntity> findByCorrelationId(String correlationId);
}
