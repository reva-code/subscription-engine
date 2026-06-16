package com.jio.customer.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * A Customer is a Party (Individual or Organization) that has a formal
 * commercial relationship with Jio — they have an account, a billing cycle,
 * and can place product orders.
 *
 * References jio_party.individual.id via engagedPartyId (cross-service,
 * no DB-level FK since they are in separate databases).
 *
 * TMF629 Customer Management API resource.
 * Table: customer
 */
@Entity
@Table(name = "customer")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Schema(name = "Customer", description = "A party that has a formal commercial relationship with Jio")
public class Customer {

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
     * Lifecycle status: active | inactive | prospect | former
     * Defaults to "prospect" on creation.
     */
    @JsonProperty("status")
    private String status = "prospect";

    @JsonProperty("statusReason")
    private @Nullable String statusReason;

    /**
     * Loyalty/tier rank: Silver | Gold | Platinum | Diamond
     */
    @JsonProperty("customerRank")
    private @Nullable String customerRank;

    // ── Cross-service reference to TMF632 Party ──────────────────────────────
    /**
     * The id of the Individual in jio_party.individual.
     * This is the link between TMF629 and TMF632.
     */
    @NotBlank
    @JsonProperty("engagedPartyId")
    private String engagedPartyId;

    @JsonProperty("engagedPartyHref")
    private @Nullable String engagedPartyHref;

    @JsonProperty("engagedPartyName")
    private @Nullable String engagedPartyName;

    // ── Timestamps ────────────────────────────────────────────────────────────
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonProperty("createdAt")
    private OffsetDateTime createdAt;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonProperty("updatedAt")
    private @Nullable OffsetDateTime updatedAt;

    // ── Characteristics (key/value pairs) ────────────────────────────────────
    @Valid
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
        name = "customer_characteristic",
        joinColumns = @JoinColumn(name = "customer_id")
    )
    @JsonProperty("characteristic")
    private List<CustomerCharacteristic> characteristic = new ArrayList<>();

    // ── TM Forum standard extension fields ───────────────────────────────────
    @JsonProperty("@baseType")
    private @Nullable String atBaseType;

    @Transient
    @JsonProperty("@schemaLocation")
    private @Nullable URI atSchemaLocation;

    @JsonProperty("@type")
    private @Nullable String atType;

    // ── Constructors ─────────────────────────────────────────────────────────
    public Customer() {}

    public Customer(String name, String engagedPartyId) {
        this.name            = name;
        this.engagedPartyId  = engagedPartyId;
    }

    // ── Getters & Setters ─────────────────────────────────────────────────────
    public String getId()                     { return id; }
    public void setId(String id)              { this.id = id; }

    public String getName()                   { return name; }
    public void setName(String name)          { this.name = name; }

    public String getStatus()                 { return status; }
    public void setStatus(String status)      { this.status = status; }

    public String getStatusReason()                       { return statusReason; }
    public void setStatusReason(String statusReason)      { this.statusReason = statusReason; }

    public String getCustomerRank()                       { return customerRank; }
    public void setCustomerRank(String customerRank)      { this.customerRank = customerRank; }

    public String getEngagedPartyId()                     { return engagedPartyId; }
    public void setEngagedPartyId(String engagedPartyId)  { this.engagedPartyId = engagedPartyId; }

    public String getEngagedPartyHref()                       { return engagedPartyHref; }
    public void setEngagedPartyHref(String engagedPartyHref)  { this.engagedPartyHref = engagedPartyHref; }

    public String getEngagedPartyName()                       { return engagedPartyName; }
    public void setEngagedPartyName(String engagedPartyName)  { this.engagedPartyName = engagedPartyName; }

    public OffsetDateTime getCreatedAt()                  { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt)    { this.createdAt = createdAt; }

    public OffsetDateTime getUpdatedAt()                  { return updatedAt; }
    public void setUpdatedAt(OffsetDateTime updatedAt)    { this.updatedAt = updatedAt; }

    public List<CustomerCharacteristic> getCharacteristic()               { return characteristic; }
    public void setCharacteristic(List<CustomerCharacteristic> characteristic) { this.characteristic = characteristic; }

    public String getAtBaseType()                 { return atBaseType; }
    public void setAtBaseType(String atBaseType)  { this.atBaseType = atBaseType; }

    public URI getAtSchemaLocation()                          { return atSchemaLocation; }
    public void setAtSchemaLocation(URI atSchemaLocation)     { this.atSchemaLocation = atSchemaLocation; }

    public String getAtType()                 { return atType; }
    public void setAtType(String atType)      { this.atType = atType; }
}
