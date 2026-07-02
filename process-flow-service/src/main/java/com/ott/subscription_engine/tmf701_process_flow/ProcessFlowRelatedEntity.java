package com.ott.subscription_engine.tmf701_process_flow;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "process_flow_related_entity")
public class ProcessFlowRelatedEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String processFlowId;
    private String href;
    private String name;
    private String role;
    @Column(name = "base_type") private String atBaseType;
    @Column(name = "type") private String atType;
    private String referredType;
    @PrePersist protected void onCreate() { atType = "RelatedEntity"; }
}
