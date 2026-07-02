package com.jio.productinventory.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * TMF637 §ProductPrice — represents one charge line on a product.
 * priceType: recurring | oneTime | usage
 * Embedded Money fields avoid a separate price table while keeping normalization for alterations.
 */
@Entity
@Table(name = "product_price")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductPrice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "name", length = 255)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @NotBlank
    @Column(name = "price_type", nullable = false, length = 100)
    private String priceType;

    @Column(name = "recurring_charge_period", length = 50)
    private String recurringChargePeriod;

    @Column(name = "unit_of_measure", length = 50)
    private String unitOfMeasure;

    // Price / Money
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

    // ProductOfferingPriceRef
    @Column(name = "pop_id", length = 36)
    private String popId;

    @Column(name = "pop_href", length = 512)
    private String popHref;

    @Column(name = "pop_name", length = 255)
    private String popName;

    // BillingAccount override
    @Column(name = "billing_account_id", length = 36)
    private String billingAccountId;

    @Column(name = "billing_account_href", length = 512)
    private String billingAccountHref;

    @Column(name = "at_type", length = 255)
    private String atType;

    @OneToMany(mappedBy = "productPrice", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<ProductPriceAlteration> productPriceAlteration = new ArrayList<>();
}
