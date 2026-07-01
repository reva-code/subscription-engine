package com.ott.subscription_engine.tmf635_usage;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "usage_related_party")
public class UsageRelatedParty {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String usageId;
    private String href;
    private String name;
    private String role;
    private String referredType;
}
