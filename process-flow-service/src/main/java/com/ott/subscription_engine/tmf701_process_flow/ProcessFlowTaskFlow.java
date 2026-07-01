package com.ott.subscription_engine.tmf701_process_flow;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "process_flow_task_flow")
public class ProcessFlowTaskFlow {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String processFlowId;
    private String href;
    private String referredType;
    @Column(name = "base_type") private String atBaseType;
    @Column(name = "type") private String atType;
    @PrePersist protected void onCreate() { atType = "TaskFlow"; }
}
