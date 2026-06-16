package com.jio.party.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;

@Entity
@Table(name = "credit_profile")
public class CreditProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NotNull
    private OffsetDateTime creditProfileDate;

    private Integer creditRiskRating;
    private Integer creditScore;

    // validFor is required on CreditProfile per TMF diagram [1]
    @NotNull
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "startDateTime", column = @Column(name = "valid_for_start")),
        @AttributeOverride(name = "endDateTime",   column = @Column(name = "valid_for_end"))
    })
    private TimePeriod validFor;

    @JsonProperty("@baseType")
    @Column(name = "at_base_type")
    private String atBaseType;

    @JsonProperty("@type")
    @Column(name = "at_type")
    private String atType;

    public String getId() { return id; }

    public OffsetDateTime getCreditProfileDate() { return creditProfileDate; }
    public void setCreditProfileDate(OffsetDateTime creditProfileDate) { this.creditProfileDate = creditProfileDate; }

    public Integer getCreditRiskRating() { return creditRiskRating; }
    public void setCreditRiskRating(Integer creditRiskRating) { this.creditRiskRating = creditRiskRating; }

    public Integer getCreditScore() { return creditScore; }
    public void setCreditScore(Integer creditScore) { this.creditScore = creditScore; }

    public TimePeriod getValidFor() { return validFor; }
    public void setValidFor(TimePeriod validFor) { this.validFor = validFor; }

    public String getAtBaseType() { return atBaseType; }
    public void setAtBaseType(String atBaseType) { this.atBaseType = atBaseType; }

    public String getAtType() { return atType; }
    public void setAtType(String atType) { this.atType = atType; }
}
