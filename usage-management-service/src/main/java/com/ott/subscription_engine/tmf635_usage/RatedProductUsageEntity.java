package com.ott.subscription_engine.tmf635_usage;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Gap 4.2 fix: RatedProductUsageEntity now has a proper @ManyToOne JPA FK to UsageEntity
 * instead of only a loose String usageId reference. This enables JPA-level joins,
 * cascading queries, and ORM-enforced referential integrity.
 */
@Data
@Entity
@Table(name = "rated_product_usage")
public class RatedProductUsageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    /**
     * Gap 4.2 fix: proper JPA foreign key relationship.
     * usageId column is kept for backward-compatible queries.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usage_id", nullable = false)
    private UsageEntity usage;

    /** Convenience accessor — delegates to the JPA relationship */
    public String getUsageId() {
        return usage != null ? usage.getId() : null;
    }

    private LocalDateTime ratingDate;

    @Column(precision = 12, scale = 4)
    private BigDecimal taxIncludedRatingAmount;

    @Column(precision = 12, scale = 4)
    private BigDecimal taxExcludedRatingAmount;

    @Column(precision = 5, scale = 2)
    private BigDecimal taxRate;

    private Boolean isTaxExempt;

    private String currencyCode;

    private String offerTariffType;

    @Column(precision = 12, scale = 4)
    private BigDecimal bucketValueConvertedInAmount;

    @Column(name = "usage_price_type")
    private String usagePriceType;

    @PrePersist
    protected void onCreate() {
        if (ratingDate == null) ratingDate = LocalDateTime.now();
        if (isTaxExempt == null) isTaxExempt = false;
        if (currencyCode == null) currencyCode = "INR";
        if (usagePriceType == null) usagePriceType = "usage";
    }
}
