package com.jio.party.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Schema(description = "A PartyRole defines a specific role played by a party (Individual or Organization) in a given context. Supported role types are Subscriber, Business Customer, ContentProvider, and Banking.")
@Entity
@Table(name = "party_role")
@EntityListeners(AuditingEntityListener.class)
public class PartyRole {

    @Schema(description = "Unique identifier of the PartyRole", example = "4079", accessMode = Schema.AccessMode.READ_ONLY)
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Schema(description = "URI reference of this PartyRole", example = "/tmf-api/partyManagement/v4/partyRole/4079", accessMode = Schema.AccessMode.READ_ONLY)
    private String href;

    @Schema(description = "Name of the party role", example = "Global Pirates", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank
    private String name;

    @Schema(description = "Current status of the party role", example = "Approved", allowableValues = {"Active", "Inactive", "Approved", "Rejected"})
    private String status;

    @Schema(description = "Reason for the current status", example = "Verified by compliance team")
    private String statusReason;

    @Schema(description = "Type of role played by the party in the platform", example = "ContentProvider", allowableValues = {"Subscriber", "Business Customer", "ContentProvider", "Banking"})
    private String roleType;

    @Schema(description = "Period during which the party role is valid")
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "startDateTime", column = @Column(name = "valid_for_start")),
        @AttributeOverride(name = "endDateTime",   column = @Column(name = "valid_for_end"))
    })
    private TimePeriod validFor;

    @Schema(description = "Base type of this object when using polymorphism", example = "PartyRole")
    @JsonProperty("@baseType")
    @Column(name = "at_base_type")
    private String atBaseType;

    @Schema(description = "URI of the schema describing this object")
    @JsonProperty("@schemaLocation")
    @Column(name = "at_schema_location")
    private String atSchemaLocation;

    @Schema(description = "Concrete type of this object", example = "PartyRole")
    @JsonProperty("@type")
    @Column(name = "at_type")
    private String atType;

    @Valid
    @Schema(description = "The party (Organization or Individual) that holds this role — e.g. the ContentProvider company")
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "engaged_party_id")
    private RelatedParty engagedParty;

    @Valid
    @Schema(description = "Other parties related to this role — e.g. the customer who contracted with this ContentProvider")
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "party_role_id")
    private List<RelatedParty> relatedParty = new ArrayList<>();

    @Valid
    @Schema(description = "Contact mediums for this party role — e.g. email, phone")
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "party_role_id")
    private List<ContactMedium> contactMedium = new ArrayList<>();

    @Valid
    @Schema(description = "Credit profiles associated with this party role")
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "party_role_id")
    private List<CreditProfile> creditProfile = new ArrayList<>();

    @Valid
    @Schema(description = "Custom characteristics for this party role — e.g. contentCategory, bankCode")
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "party_role_id")
    private List<Characteristic> characteristic = new ArrayList<>();

    @Valid
    @Schema(description = "Accounts linked to this party role")
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "party_role_id")
    private List<AccountRef> account = new ArrayList<>();

    @Valid
    @Schema(description = "Payment methods associated with this party role")
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "party_role_id")
    private List<PaymentMethodRef> paymentMethod = new ArrayList<>();

    @Valid
    @Schema(description = "Agreements linked to this party role")
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "party_role_id")
    private List<AgreementRef> agreement = new ArrayList<>();

    @Schema(description = "Timestamp when this record was created", accessMode = Schema.AccessMode.READ_ONLY)
    @CreatedDate
    @Column(name = "created_date", updatable = false)
    private OffsetDateTime createdDate;

    @Schema(description = "Timestamp when this record was last modified", accessMode = Schema.AccessMode.READ_ONLY)
    @LastModifiedDate
    @Column(name = "last_modified_date")
    private OffsetDateTime lastModifiedDate;

    @Version
    @Column(name = "version")
    private Long version;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getHref() { return href; }
    public void setHref(String href) { this.href = href; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getStatusReason() { return statusReason; }
    public void setStatusReason(String statusReason) { this.statusReason = statusReason; }

    public String getRoleType() { return roleType; }
    public void setRoleType(String roleType) { this.roleType = roleType; }

    public TimePeriod getValidFor() { return validFor; }
    public void setValidFor(TimePeriod validFor) { this.validFor = validFor; }

    public String getAtBaseType() { return atBaseType; }
    public void setAtBaseType(String atBaseType) { this.atBaseType = atBaseType; }

    public String getAtSchemaLocation() { return atSchemaLocation; }
    public void setAtSchemaLocation(String atSchemaLocation) { this.atSchemaLocation = atSchemaLocation; }

    public String getAtType() { return atType; }
    public void setAtType(String atType) { this.atType = atType; }

    public RelatedParty getEngagedParty() { return engagedParty; }
    public void setEngagedParty(RelatedParty engagedParty) { this.engagedParty = engagedParty; }

    public List<RelatedParty> getRelatedParty() { return relatedParty; }
    public void setRelatedParty(List<RelatedParty> relatedParty) { this.relatedParty = relatedParty; }

    public List<ContactMedium> getContactMedium() { return contactMedium; }
    public void setContactMedium(List<ContactMedium> contactMedium) { this.contactMedium = contactMedium; }

    public List<CreditProfile> getCreditProfile() { return creditProfile; }
    public void setCreditProfile(List<CreditProfile> creditProfile) { this.creditProfile = creditProfile; }

    public List<Characteristic> getCharacteristic() { return characteristic; }
    public void setCharacteristic(List<Characteristic> characteristic) { this.characteristic = characteristic; }

    public List<AccountRef> getAccount() { return account; }
    public void setAccount(List<AccountRef> account) { this.account = account; }

    public List<PaymentMethodRef> getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(List<PaymentMethodRef> paymentMethod) { this.paymentMethod = paymentMethod; }

    public List<AgreementRef> getAgreement() { return agreement; }
    public void setAgreement(List<AgreementRef> agreement) { this.agreement = agreement; }

    public OffsetDateTime getCreatedDate() { return createdDate; }
    public OffsetDateTime getLastModifiedDate() { return lastModifiedDate; }
    public Long getVersion() { return version; }
}
