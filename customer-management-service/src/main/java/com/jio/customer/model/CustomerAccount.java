package com.jio.customer.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;

import java.net.URI;
import java.time.OffsetDateTime;

/**
 * A CustomerAccount is the financial account belonging to a Customer.
 * It holds account type (prepaid/postpaid), credit limit, billing day,
 * and payment status. This is what gets referenced by Product Inventory
 * (TMF637) and Payment Management (TMF676).
 *
 * TMF629 Customer Management API resource.
 * Table: customer_account  (FK → customer.id)
 */
@Entity
@Table(name = "customer_account")
@Schema(name = "CustomerAccount", description = "Financial account belonging to a Customer — holds billing and payment info")
public class CustomerAccount {

    // ── Primary Key ─────────────────────────────────────────────────────────
    @Id
    @UuidGenerator
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private String id;

    // ── Core fields ──────────────────────────────────────────────────────────
    @NotBlank
    @JsonProperty("name")
    private String name;

    /**
     * Account type: PREPAID | POSTPAID | ENTERPRISE
     * Determines billing behavior downstream.
     */
    @NotBlank
    @JsonProperty("accountType")
    private String accountType;

    /**
     * Lifecycle status: active | inactive | closed
     */
    @JsonProperty("status")
    private String status = "active";

    /** Credit limit in INR (relevant for POSTPAID accounts) */
    @JsonProperty("creditLimit")
    private @Nullable Float creditLimit;

    /** Currency code — always INR for Jio */
    @JsonProperty("currency")
    private String currency = "INR";

    /**
     * Day of the month on which the bill is generated (1–28).
     * e.g. 5 = bill generated on the 5th of every month.
     */
    @JsonProperty("billDay")
    private @Nullable Integer billDay;

    /**
     * Current payment status: PAID | UNPAID | OVERDUE
     */
    @JsonProperty("paymentStatus")
    private String paymentStatus = "PAID";

    // ── FK to Customer ────────────────────────────────────────────────────────
    /**
     * The customer this account belongs to.
     * Many accounts can belong to one customer (e.g. personal + business).
     */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    @JsonProperty("customer")
    private Customer customer;

    // ── Timestamps ────────────────────────────────────────────────────────────
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonProperty("createdAt")
    private OffsetDateTime createdAt;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonProperty("updatedAt")
    private @Nullable OffsetDateTime updatedAt;

    // ── TM Forum standard extension fields ───────────────────────────────────
    @JsonProperty("@baseType")
    private @Nullable String atBaseType;

    @Transient
    @JsonProperty("@schemaLocation")
    private @Nullable URI atSchemaLocation;

    @JsonProperty("@type")
    private @Nullable String atType;

    // ── Constructors ─────────────────────────────────────────────────────────
    public CustomerAccount() {}

    public CustomerAccount(String name, String accountType, Customer customer) {
        this.name        = name;
        this.accountType = accountType;
        this.customer    = customer;
    }

    // ── Getters & Setters ─────────────────────────────────────────────────────
    public String getId()                 { return id; }
    public void setId(String id)          { this.id = id; }

    public String getName()               { return name; }
    public void setName(String name)      { this.name = name; }

    public String getAccountType()                    { return accountType; }
    public void setAccountType(String accountType)    { this.accountType = accountType; }

    public String getStatus()             { return status; }
    public void setStatus(String status)  { this.status = status; }

    public Float getCreditLimit()                 { return creditLimit; }
    public void setCreditLimit(Float creditLimit) { this.creditLimit = creditLimit; }

    public String getCurrency()               { return currency; }
    public void setCurrency(String currency)  { this.currency = currency; }

    public Integer getBillDay()               { return billDay; }
    public void setBillDay(Integer billDay)   { this.billDay = billDay; }

    public String getPaymentStatus()                      { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus)    { this.paymentStatus = paymentStatus; }

    public Customer getCustomer()                 { return customer; }
    public void setCustomer(Customer customer)    { this.customer = customer; }

    public OffsetDateTime getCreatedAt()                  { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt)    { this.createdAt = createdAt; }

    public OffsetDateTime getUpdatedAt()                  { return updatedAt; }
    public void setUpdatedAt(OffsetDateTime updatedAt)    { this.updatedAt = updatedAt; }

    public String getAtBaseType()                 { return atBaseType; }
    public void setAtBaseType(String atBaseType)  { this.atBaseType = atBaseType; }

    public URI getAtSchemaLocation()                          { return atSchemaLocation; }
    public void setAtSchemaLocation(URI atSchemaLocation)     { this.atSchemaLocation = atSchemaLocation; }

    public String getAtType()                 { return atType; }
    public void setAtType(String atType)      { this.atType = atType; }
}
