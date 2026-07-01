package com.ott.subscription_engine.tmf635_usage;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "usage_spec_characteristic")
public class UsageSpecCharacteristic {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String usageSpecificationId;
    private Boolean configurable;
    private String description;
    private Boolean extensible;
    private Boolean isUnique;
    private Integer maxCardinality;
    private Integer minCardinality;
    private String name;
    private String regex;
    private String valueType;
    private LocalDateTime validForStart;
    private LocalDateTime validForEnd;
    @Column(name = "base_type") private String atBaseType;
    @Column(name = "type") private String atType;
    @PrePersist protected void onCreate() { atType = "CharacteristicSpecification"; }
}
