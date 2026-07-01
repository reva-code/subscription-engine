package com.ott.subscription_engine.tmf635_usage;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "usage_specification")
public class UsageSpecificationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String href;
    private String name;
    private String description;
    
    private String lifecycleStatus;
    
    /** Hour | View | GB */
    private String unitOfMeasure;
    
    private BigDecimal unitPrice;
    private Long baseQuota;
    private String currency;

    @JsonProperty("@type")
    @Column(name = "type")
    private String atType;

    @JsonProperty("@baseType")
    @Column(name = "base_type")
    private String atBaseType;

    @JsonProperty("@schemaLocation")
    @Column(name = "schema_location")
    private String atSchemaLocation;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        atType = "UsageSpecification";
        atBaseType = "UsageSpecification";
        atSchemaLocation = "https://raw.githubusercontent.com/tmforum-apis/TMF635_UsageManagement/main/TMF635-UsageManagement-v4.0.0.swagger.json";
        if (lifecycleStatus == null) lifecycleStatus = "Active";
        if (unitPrice == null) unitPrice = BigDecimal.ZERO;
        if (currency == null) currency = "INR";
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
