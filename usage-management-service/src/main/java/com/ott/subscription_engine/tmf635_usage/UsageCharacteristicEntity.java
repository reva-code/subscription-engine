package com.ott.subscription_engine.tmf635_usage;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Gap 4.1 fix: UsageCharacteristic is now a proper @Entity persisted in its own table.
 * Previously, only "quantity" and "unit" were stored as flat columns on UsageEntity,
 * silently dropping all other characteristics (e.g. sessionId, ipAddress, serviceType).
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "usage_characteristic")
public class UsageCharacteristicEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    /** FK back to the parent Usage record */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usage_id", nullable = false)
    private UsageEntity usage;

    /** TMF635 characteristic name (e.g. "quantity", "unit", "sessionId", "ipAddress") */
    @Column(nullable = false)
    private String name;

    /** Value stored as String; callers are responsible for type-casting */
    private String value;

    /** Optional: TMF characteristic value type (e.g. "integer", "string", "float") */
    private String valueType;

    public UsageCharacteristicEntity(UsageEntity usage, String name, String value) {
        this.usage = usage;
        this.name = name;
        this.value = value;
    }
}
