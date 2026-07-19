package com.apex.payment.gateway.dto;

import java.util.List;
import java.util.Map;

public class GatewayWebhookEvent {
    private String id; // Unique event ID from gateway
    private String entity;
    private String accountId;
    private String event; // e.g. payment.authorized, payment.captured, payment.failed, refund.processed
    private List<String> contains;
    private Map<String, Object> payload;
    private long createdAt;

    public GatewayWebhookEvent() {}

    public GatewayWebhookEvent(String id, String entity, String accountId, String event, List<String> contains,
                               Map<String, Object> payload, long createdAt) {
        this.id = id;
        this.entity = entity;
        this.accountId = accountId;
        this.event = event;
        this.contains = contains;
        this.payload = payload;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public List<String> getContains() {
        return contains;
    }

    public void setContains(List<String> contains) {
        this.contains = contains;
    }

    public Map<String, Object> getPayload() {
        return payload;
    }

    public void setPayload(Map<String, Object> payload) {
        this.payload = payload;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String id;
        private String entity;
        private String accountId;
        private String event;
        private List<String> contains;
        private Map<String, Object> payload;
        private long createdAt;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder entity(String entity) {
            this.entity = entity;
            return this;
        }

        public Builder accountId(String accountId) {
            this.accountId = accountId;
            return this;
        }

        public Builder event(String event) {
            this.event = event;
            return this;
        }

        public Builder contains(List<String> contains) {
            this.contains = contains;
            return this;
        }

        public Builder payload(Map<String, Object> payload) {
            this.payload = payload;
            return this;
        }

        public Builder createdAt(long createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public GatewayWebhookEvent build() {
            return new GatewayWebhookEvent(id, entity, accountId, event, contains, payload, createdAt);
        }
    }
}
