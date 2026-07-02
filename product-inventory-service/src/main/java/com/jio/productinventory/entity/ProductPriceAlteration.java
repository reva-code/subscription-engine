package com.jio.productinventory.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.math.BigDecimal;

/** TMF637 §PriceAlteration — discount or surcharge applied to a ProductPrice. */
@Entity
@Table(name = "product_price_alteration")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ProductPriceAlteration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_price_id", nullable = false)
    private ProductPrice productPrice;

    @Column(name = "name", length = 255)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "application_duration")
    private Integer applicationDuration;

    @NotBlank
    @Column(name = "price_type", nullable = false, length = 100)
    private String priceType;

    @Column(name = "recurring_charge_period", length = 50)
    private String recurringChargePeriod;

    @Column(name = "unit_of_measure", length = 50)
    private String unitOfMeasure;

    @Column(name = "priority")
    private Integer priority;

    @Column(name = "duty_free_amount", precision = 18, scale = 4)
    private BigDecimal dutyFreeAmount;

    @Column(name = "tax_included_amount", precision = 18, scale = 4)
    private BigDecimal taxIncludedAmount;

    @Column(name = "tax_rate", precision = 7, scale = 4)
    private BigDecimal taxRate;

    @Column(name = "percentage", precision = 7, scale = 4)
    private BigDecimal percentage;

    @Column(name = "currency_unit", length = 3)
    private String currencyUnit;

    @Column(name = "pop_id", length = 36)
    private String popId;

    @Column(name = "pop_href", length = 512)
    private String popHref;

    @Column(name = "pop_name", length = 255)
    private String popName;

    @Column(name = "at_type", length = 255)
    private String atType;
}
