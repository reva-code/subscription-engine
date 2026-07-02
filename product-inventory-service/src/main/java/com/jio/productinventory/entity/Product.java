package com.jio.productinventory.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * TMF637 §Product — system of record for a customer-owned telecom product.
 *
 * Design decisions:
 * - UUID primary key for cross-service href generation.
 * - Inline scalar refs (BillingAccountRef, ProductOfferingRef, ProductSpecificationRef)
 *   avoid join overhead for the most-read fields.
 * - One-to-many collections use CascadeType.ALL with orphanRemoval=true so
 *   PATCH operations on the parent transparently manage child rows.
 * - @Version provides optimistic locking; essential for concurrent order processing.
 */
@Entity
@Table(name = "product")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @Column(name = "id", nullable = false, length = 36)
    private String id;

    @Column(name = "href", length = 512)
    private String href;

    @Column(name = "name", length = 255)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "is_bundle", nullable = false)
    @Builder.Default
    private Boolean isBundle = false;

    @Column(name = "is_customer_visible", nullable = false)
    @Builder.Default
    private Boolean isCustomerVisible = true;

    @Column(name = "product_serial_number", length = 255)
    private String productSerialNumber;

    @Column(name = "order_date")
    private OffsetDateTime orderDate;

    @Column(name = "start_date")
    private OffsetDateTime startDate;

    @Column(name = "termination_date")
    private OffsetDateTime terminationDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    private ProductStatusType status;

    // ── Inline ProductOfferingRef ───────────────────────────────
    @Column(name = "product_offering_id", length = 36)
    private String productOfferingId;

    @Column(name = "product_offering_href", length = 512)
    private String productOfferingHref;

    @Column(name = "product_offering_name", length = 255)
    private String productOfferingName;

    // ── Inline ProductSpecificationRef ─────────────────────────
    @Column(name = "product_spec_id", length = 36)
    private String productSpecId;

    @Column(name = "product_spec_href", length = 512)
    private String productSpecHref;

    @Column(name = "product_spec_name", length = 255)
    private String productSpecName;

    @Column(name = "product_spec_version", length = 50)
    private String productSpecVersion;

    // ── Inline BillingAccountRef ───────────────────────────────
    @Column(name = "billing_account_id", length = 36)
    private String billingAccountId;

    @Column(name = "billing_account_href", length = 512)
    private String billingAccountHref;

    @Column(name = "billing_account_name", length = 255)
    private String billingAccountName;

    // ── TMF polymorphism fields ────────────────────────────────
    @Column(name = "at_base_type", length = 255)
    @Builder.Default
    private String atBaseType = "Product";

    @Column(name = "at_schema_location", length = 512)
    private String atSchemaLocation;

    @Column(name = "at_type", length = 255)
    @Builder.Default
    private String atType = "Product";

    // ── Collections ───────────────────────────────────────────
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<ProductCharacteristic> productCharacteristic = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<ProductRelationship> productRelationship = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<ProductPrice> productPrice = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<ProductTerm> productTerm = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<RelatedParty> relatedParty = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<ProductPlace> place = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<ProductOrderRef> productOrderItem = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<AgreementRef> agreement = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<ProductNote> note = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<ProductAttachment> attachment = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<RealizingResource> realizingResource = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<RealizingService> realizingService = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<ProductStatusHistory> statusHistory = new ArrayList<>();

    // ── Auditing ──────────────────────────────────────────────
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @CreatedBy
    @Column(name = "created_by", length = 100, updatable = false)
    private String createdBy;

    @LastModifiedBy
    @Column(name = "updated_by", length = 100)
    private String updatedBy;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    // ── Helpers ───────────────────────────────────────────────
    public void addCharacteristic(ProductCharacteristic c) {
        c.setProduct(this);
        productCharacteristic.add(c);
    }

    public void addRelationship(ProductRelationship r) {
        r.setProduct(this);
        productRelationship.add(r);
    }

    public void addPrice(ProductPrice p) {
        p.setProduct(this);
        productPrice.add(p);
    }
}
