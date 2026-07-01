package com.ott.subscription_engine.tmf701_process_flow;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "process_flow_characteristic")
public class ProcessFlowCharacteristic {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String processFlowId;
    private String name;
    private String value;
    private String valueType;
    @Column(name = "base_type") private String atBaseType;
    @Column(name = "type") private String atType;
    @PrePersist protected void onCreate() { atType = "Characteristic"; }
}
