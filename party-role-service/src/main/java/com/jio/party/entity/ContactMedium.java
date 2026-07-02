package com.jio.party.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "contact_medium")
public class ContactMedium {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotBlank
    private String mediumType;

    private Boolean preferred;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "startDateTime", column = @Column(name = "valid_for_start")),
        @AttributeOverride(name = "endDateTime",   column = @Column(name = "valid_for_end"))
    })
    private TimePeriod validFor;

    @Valid
    @Embedded
    private MediumCharacteristic characteristic;

    @JsonProperty("@baseType")
    @Column(name = "at_base_type")
    private String atBaseType;

    @JsonProperty("@schemaLocation")
    @Column(name = "at_schema_location")
    private String atSchemaLocation;

    @JsonProperty("@type")
    @Column(name = "at_type")
    private String atType;

    public String getId() { return id; }

    public String getMediumType() { return mediumType; }
    public void setMediumType(String mediumType) { this.mediumType = mediumType; }

    public Boolean getPreferred() { return preferred; }
    public void setPreferred(Boolean preferred) { this.preferred = preferred; }

    public TimePeriod getValidFor() { return validFor; }
    public void setValidFor(TimePeriod validFor) { this.validFor = validFor; }

    public MediumCharacteristic getCharacteristic() { return characteristic; }
    public void setCharacteristic(MediumCharacteristic characteristic) { this.characteristic = characteristic; }

    public String getAtBaseType() { return atBaseType; }
    public void setAtBaseType(String atBaseType) { this.atBaseType = atBaseType; }

    public String getAtSchemaLocation() { return atSchemaLocation; }
    public void setAtSchemaLocation(String atSchemaLocation) { this.atSchemaLocation = atSchemaLocation; }

    public String getAtType() { return atType; }
    public void setAtType(String atType) { this.atType = atType; }
}
