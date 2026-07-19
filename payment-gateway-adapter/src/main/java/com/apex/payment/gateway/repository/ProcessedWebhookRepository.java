package com.apex.payment.gateway.repository;

import com.apex.payment.gateway.entity.ProcessedWebhook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProcessedWebhookRepository extends JpaRepository<ProcessedWebhook, Long> {
    boolean existsByEventId(String eventId);
    Optional<ProcessedWebhook> findByEventId(String eventId);
}
