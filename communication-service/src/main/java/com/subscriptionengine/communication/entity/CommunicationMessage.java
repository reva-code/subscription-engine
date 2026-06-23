package com.subscriptionengine.communication.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "communication_messages")
public class CommunicationMessage {

    @Id
    @Column(length = 36)
    private String id;

    @Column(name = "message_reference")
    private String messageReference;

    @Column(name = "customer_id")
    private String customerId;

    @Column(name = "receiver_email")
    private String receiverEmail;

    @Column(name = "receiver_mobile")
    private String receiverMobile;

    @Enumerated(EnumType.STRING)
    @Column(name = "communication_type", nullable = false)
    private CommunicationType communicationType;

    @Column(name = "subject")
    private String subject;

    @Lob
    @Column(name = "message_body", columnDefinition = "TEXT")
    private String messageBody;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CommunicationStatus status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "provider")
    private String provider;

    @Column(name = "provider_message_id")
    private String providerMessageId;

    @Column(name = "delivery_status")
    private String deliveryStatus;

    @Column(name = "delivery_timestamp")
    private LocalDateTime deliveryTimestamp;

    public CommunicationMessage() {
    }

    public CommunicationMessage(String id, String messageReference, String customerId, String receiverEmail, String receiverMobile, 
                                CommunicationType communicationType, String subject, String messageBody, CommunicationStatus status, 
                                LocalDateTime createdAt, String provider, String providerMessageId, String deliveryStatus, LocalDateTime deliveryTimestamp) {
        this.id = id;
        this.messageReference = messageReference;
        this.customerId = customerId;
        this.receiverEmail = receiverEmail;
        this.receiverMobile = receiverMobile;
        this.communicationType = communicationType;
        this.subject = subject;
        this.messageBody = messageBody;
        this.status = status;
        this.createdAt = createdAt;
        this.provider = provider;
        this.providerMessageId = providerMessageId;
        this.deliveryStatus = deliveryStatus;
        this.deliveryTimestamp = deliveryTimestamp;
    }

    @PrePersist
    protected void onCreate() {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();
        }
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getMessageReference() { return messageReference; }
    public void setMessageReference(String messageReference) { this.messageReference = messageReference; }

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public String getReceiverEmail() { return receiverEmail; }
    public void setReceiverEmail(String receiverEmail) { this.receiverEmail = receiverEmail; }

    public String getReceiverMobile() { return receiverMobile; }
    public void setReceiverMobile(String receiverMobile) { this.receiverMobile = receiverMobile; }

    public CommunicationType getCommunicationType() { return communicationType; }
    public void setCommunicationType(CommunicationType communicationType) { this.communicationType = communicationType; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getMessageBody() { return messageBody; }
    public void setMessageBody(String messageBody) { this.messageBody = messageBody; }

    public CommunicationStatus getStatus() { return status; }
    public void setStatus(CommunicationStatus status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public String getProvider() { return provider; }
    public void setProvider(String provider) { this.provider = provider; }

    public String getProviderMessageId() { return providerMessageId; }
    public void setProviderMessageId(String providerMessageId) { this.providerMessageId = providerMessageId; }

    public String getDeliveryStatus() { return deliveryStatus; }
    public void setDeliveryStatus(String deliveryStatus) { this.deliveryStatus = deliveryStatus; }

    public LocalDateTime getDeliveryTimestamp() { return deliveryTimestamp; }
    public void setDeliveryTimestamp(LocalDateTime deliveryTimestamp) { this.deliveryTimestamp = deliveryTimestamp; }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String id;
        private String messageReference;
        private String customerId;
        private String receiverEmail;
        private String receiverMobile;
        private CommunicationType communicationType;
        private String subject;
        private String messageBody;
        private CommunicationStatus status;
        private LocalDateTime createdAt;
        private String provider;
        private String providerMessageId;
        private String deliveryStatus;
        private LocalDateTime deliveryTimestamp;

        public Builder id(String id) { this.id = id; return this; }
        public Builder messageReference(String messageReference) { this.messageReference = messageReference; return this; }
        public Builder customerId(String customerId) { this.customerId = customerId; return this; }
        public Builder receiverEmail(String receiverEmail) { this.receiverEmail = receiverEmail; return this; }
        public Builder receiverMobile(String receiverMobile) { this.receiverMobile = receiverMobile; return this; }
        public Builder communicationType(CommunicationType communicationType) { this.communicationType = communicationType; return this; }
        public Builder subject(String subject) { this.subject = subject; return this; }
        public Builder messageBody(String messageBody) { this.messageBody = messageBody; return this; }
        public Builder status(CommunicationStatus status) { this.status = status; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public Builder provider(String provider) { this.provider = provider; return this; }
        public Builder providerMessageId(String providerMessageId) { this.providerMessageId = providerMessageId; return this; }
        public Builder deliveryStatus(String deliveryStatus) { this.deliveryStatus = deliveryStatus; return this; }
        public Builder deliveryTimestamp(LocalDateTime deliveryTimestamp) { this.deliveryTimestamp = deliveryTimestamp; return this; }

        public CommunicationMessage build() {
            return new CommunicationMessage(id, messageReference, customerId, receiverEmail, receiverMobile, communicationType, 
                                            subject, messageBody, status, createdAt, provider, providerMessageId, deliveryStatus, deliveryTimestamp);
        }
    }
}
