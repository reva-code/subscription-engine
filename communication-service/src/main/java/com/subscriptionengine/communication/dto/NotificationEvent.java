package com.subscriptionengine.communication.dto;

import java.time.OffsetDateTime;
import java.util.Map;

public class NotificationEvent {
    private String eventId;
    private OffsetDateTime eventTime;
    private String eventType;
    private String customerId;
    private String receiverEmail;
    private String receiverMobile;
    private Map<String, String> templateParameters;

    public NotificationEvent() {
    }

    public NotificationEvent(String eventId, OffsetDateTime eventTime, String eventType, String customerId, String receiverEmail, String receiverMobile, Map<String, String> templateParameters) {
        this.eventId = eventId;
        this.eventTime = eventTime;
        this.eventType = eventType;
        this.customerId = customerId;
        this.receiverEmail = receiverEmail;
        this.receiverMobile = receiverMobile;
        this.templateParameters = templateParameters;
    }

    public String getEventId() { return eventId; }
    public void setEventId(String eventId) { this.eventId = eventId; }

    public OffsetDateTime getEventTime() { return eventTime; }
    public void setEventTime(OffsetDateTime eventTime) { this.eventTime = eventTime; }

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public String getReceiverEmail() { return receiverEmail; }
    public void setReceiverEmail(String receiverEmail) { this.receiverEmail = receiverEmail; }

    public String getReceiverMobile() { return receiverMobile; }
    public void setReceiverMobile(String receiverMobile) { this.receiverMobile = receiverMobile; }

    public Map<String, String> getTemplateParameters() { return templateParameters; }
    public void setTemplateParameters(Map<String, String> templateParameters) { this.templateParameters = templateParameters; }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String eventId;
        private OffsetDateTime eventTime;
        private String eventType;
        private String customerId;
        private String receiverEmail;
        private String receiverMobile;
        private Map<String, String> templateParameters;

        public Builder eventId(String eventId) { this.eventId = eventId; return this; }
        public Builder eventTime(OffsetDateTime eventTime) { this.eventTime = eventTime; return this; }
        public Builder eventType(String eventType) { this.eventType = eventType; return this; }
        public Builder customerId(String customerId) { this.customerId = customerId; return this; }
        public Builder receiverEmail(String receiverEmail) { this.receiverEmail = receiverEmail; return this; }
        public Builder receiverMobile(String receiverMobile) { this.receiverMobile = receiverMobile; return this; }
        public Builder templateParameters(Map<String, String> templateParameters) { this.templateParameters = templateParameters; return this; }

        public NotificationEvent build() {
            return new NotificationEvent(eventId, eventTime, eventType, customerId, receiverEmail, receiverMobile, templateParameters);
        }
    }
}
