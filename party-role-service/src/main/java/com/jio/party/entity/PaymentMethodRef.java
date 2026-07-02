package com.jio.party.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "payment_method_ref")
public class PaymentMethodRef {

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "db_id")
    private String dbId;

    @NotBlank
    private String id;

    private String href;
    private String name;

    @JsonProperty("@referredType")
    @Column(name = "at_referred_type")
    private String atReferredType;

    @JsonProperty("@baseType")
    @Column(name = "at_base_type")
    private String atBaseType;

    @JsonProperty("@schemaLocation")
    @Column(name = "at_schema_location")
    private String atSchemaLocation;

    @JsonProperty("@type")
    @Column(name = "at_type")
    private String atType;

    public String getDbId() { return dbId; }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getHref() { return href; }
    public void setHref(String href) { this.href = href; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAtReferredType() { return atReferredType; }
    public void setAtReferredType(String atReferredType) { this.atReferredType = atReferredType; }

    public String getAtBaseType() { return atBaseType; }
    public void setAtBaseType(String atBaseType) { this.atBaseType = atBaseType; }

    public String getAtSchemaLocation() { return atSchemaLocation; }
    public void setAtSchemaLocation(String atSchemaLocation) { this.atSchemaLocation = atSchemaLocation; }

    public String getAtType() { return atType; }
    public void setAtType(String atType) { this.atType = atType; }
}
