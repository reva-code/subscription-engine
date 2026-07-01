package com.ott.subscription_engine.tmf701_process_flow;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "process_flow_related_party")
public class ProcessFlowRelatedParty {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String processFlowId;
    private String href;
    private String name;
    private String role;
    private String referredType;
}
