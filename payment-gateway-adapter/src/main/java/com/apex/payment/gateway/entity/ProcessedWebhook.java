package com.apex.payment.gateway.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "processed_webhooks", indexes = {
        @Index(name = "idx_processed_webhooks_event_id", columnList = "event_id", unique = true)
})
public class ProcessedWebhook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_id", nullable = false, unique = true)
    private String eventId;

    @Column(name = "processed_at", nullable = false)
    private Instant processedAt = Instant.now();

    public ProcessedWebhook() {}

    public ProcessedWebhook(Long id, String eventId, Instant processedAt) {
        this.id = id;
        this.eventId = eventId;
        this.processedAt = processedAt != null ? processedAt : Instant.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public Instant getProcessedAt() {
        return processedAt;
    }

    public void setProcessedAt(Instant processedAt) {
        this.processedAt = processedAt;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private String eventId;
        private Instant processedAt;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder eventId(String eventId) {
            this.eventId = eventId;
            return this;
        }

        public Builder processedAt(Instant processedAt) {
            this.processedAt = processedAt;
            return this;
        }

        public ProcessedWebhook build() {
            return new ProcessedWebhook(id, eventId, processedAt);
        }
    }
}
