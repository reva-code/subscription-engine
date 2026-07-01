package com.ott.subscription_engine.tmf635_usage;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "product_usage")
public class UsageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String href;

    @NotNull(message = "Product ID reference is required")
    private String productId;

    private String customerId;
    private String subscriptionId;

    private LocalDateTime usageDate;
    private String description;
    private String usageType;

    /**
     * Gap 4.1 fix: UsageCharacteristic is now a proper child entity collection.
     * All arbitrary key-value pairs are stored in the usage_characteristic table
     * instead of being silently dropped if they are not "quantity" or "unit".
     */
    @OneToMany(mappedBy = "usage", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<UsageCharacteristicEntity> characteristics = new ArrayList<>();

    /** Received | Rated | Invoiced */
    private String status;

    private BigDecimal ratedAmount;

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

    public Long getQuantity() {
        if (characteristics == null) return null;
        return characteristics.stream()
                .filter(c -> "quantity".equalsIgnoreCase(c.getName()))
                .map(c -> {
                    try {
                        return Long.parseLong(c.getValue());
                    } catch (Exception e) {
                        return null;
                    }
                })
                .filter(java.util.Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    public void setQuantity(Long quantity) {
        if (characteristics == null) {
            characteristics = new ArrayList<>();
        }
        characteristics.removeIf(c -> "quantity".equalsIgnoreCase(c.getName()));
        if (quantity != null) {
            UsageCharacteristicEntity charEntity = new UsageCharacteristicEntity(this, "quantity", String.valueOf(quantity));
            characteristics.add(charEntity);
        }
    }

    public void setQuantity(long quantity) {
        setQuantity(Long.valueOf(quantity));
    }

    public String getUnit() {
        if (characteristics == null) return null;
        return characteristics.stream()
                .filter(c -> "unit".equalsIgnoreCase(c.getName()))
                .map(UsageCharacteristicEntity::getValue)
                .findFirst()
                .orElse(null);
    }

    public void setUnit(String unit) {
        if (characteristics == null) {
            characteristics = new ArrayList<>();
        }
        characteristics.removeIf(c -> "unit".equalsIgnoreCase(c.getName()));
        if (unit != null) {
            UsageCharacteristicEntity charEntity = new UsageCharacteristicEntity(this, "unit", unit);
            characteristics.add(charEntity);
        }
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        atType = "Usage";
        atBaseType = "Usage";
        atSchemaLocation = "https://raw.githubusercontent.com/tmforum-apis/TMF635_UsageManagement/main/TMF635-UsageManagement-v4.0.0.swagger.json";
        if (status == null) status = "received";
        if (usageDate == null) usageDate = LocalDateTime.now();
        if (ratedAmount == null) ratedAmount = BigDecimal.ZERO;
    }
}
