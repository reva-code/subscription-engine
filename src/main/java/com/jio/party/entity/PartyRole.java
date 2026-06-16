package com.jio.party.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "party_role")
public class PartyRole {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String href;

    @NotBlank
    private String name;

    private String status;
    private String statusReason;

    // "ContentProvider" or "Banking" — implementation-defined, not enumerated in TMF669
    private String roleType;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "startDateTime", column = @Column(name = "valid_for_start")),
        @AttributeOverride(name = "endDateTime",   column = @Column(name = "valid_for_end"))
    })
    private TimePeriod validFor;

    @JsonProperty("@baseType")
    @Column(name = "at_base_type")
    private String atBaseType;

    @JsonProperty("@schemaLocation")
    @Column(name = "at_schema_location")
    private String atSchemaLocation;

    @JsonProperty("@type")
    @Column(name = "at_type")
    private String atType;

    // The party (org/individual) that holds this role — 0..1
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "engaged_party_id")
    private RelatedParty engagedParty;

    // Other parties related to this role (e.g. the customer) — 0..*
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "party_role_id")
    private List<RelatedParty> relatedParty = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "party_role_id")
    private List<ContactMedium> contactMedium = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "party_role_id")
    private List<CreditProfile> creditProfile = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "party_role_id")
    private List<Characteristic> characteristic = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "party_role_id")
    private List<AccountRef> account = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "party_role_id")
    private List<PaymentMethodRef> paymentMethod = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "party_role_id")
    private List<AgreementRef> agreement = new ArrayList<>();

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
}
