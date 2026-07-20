package com.subscriptionengine.communication.generated.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.subscriptionengine.communication.generated.model.RelatedParty;
import java.net.URI;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * Sender of the communication message.
 */

@Schema(name = "Sender", description = "Sender of the communication message.")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-07-20T15:01:51.572067+05:30[Asia/Calcutta]", comments = "Generator version: 7.7.0")
public class Sender {

  private String id;

  private String email;

  private String name;

  private String phoneNumber;

  private RelatedParty party;

  private String atBaseType;

  private URI atSchemaLocation;

  private String atType;

  public Sender id(String id) {
    this.id = id;
    return this;
  }

  /**
   * ID of the sender
   * @return id
   */
  
  @Schema(name = "id", description = "ID of the sender", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("id")
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Sender email(String email) {
    this.email = email;
    return this;
  }

  /**
   * Sender address of email, if the communication type is email
   * @return email
   */
  
  @Schema(name = "email", description = "Sender address of email, if the communication type is email", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("email")
  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Sender name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Name of the sender
   * @return name
   */
  
  @Schema(name = "name", description = "Name of the sender", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Sender phoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
    return this;
  }

  /**
   * Phone number of the sender, if the communication type is SMS.
   * @return phoneNumber
   */
  
  @Schema(name = "phoneNumber", description = "Phone number of the sender, if the communication type is SMS.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("phoneNumber")
  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public Sender party(RelatedParty party) {
    this.party = party;
    return this;
  }

  /**
   * Get party
   * @return party
   */
  @Valid 
  @Schema(name = "party", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("party")
  public RelatedParty getParty() {
    return party;
  }

  public void setParty(RelatedParty party) {
    this.party = party;
  }

  public Sender atBaseType(String atBaseType) {
    this.atBaseType = atBaseType;
    return this;
  }

  /**
   * When sub-classing, this defines the super-class
   * @return atBaseType
   */
  
  @Schema(name = "@baseType", description = "When sub-classing, this defines the super-class", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("@baseType")
  public String getAtBaseType() {
    return atBaseType;
  }

  public void setAtBaseType(String atBaseType) {
    this.atBaseType = atBaseType;
  }

  public Sender atSchemaLocation(URI atSchemaLocation) {
    this.atSchemaLocation = atSchemaLocation;
    return this;
  }

  /**
   * A URI to a JSON-Schema file that defines additional attributes and relationships
   * @return atSchemaLocation
   */
  @Valid 
  @Schema(name = "@schemaLocation", description = "A URI to a JSON-Schema file that defines additional attributes and relationships", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("@schemaLocation")
  public URI getAtSchemaLocation() {
    return atSchemaLocation;
  }

  public void setAtSchemaLocation(URI atSchemaLocation) {
    this.atSchemaLocation = atSchemaLocation;
  }

  public Sender atType(String atType) {
    this.atType = atType;
    return this;
  }

  /**
   * When sub-classing, this defines the sub-class entity name
   * @return atType
   */
  
  @Schema(name = "@type", description = "When sub-classing, this defines the sub-class entity name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("@type")
  public String getAtType() {
    return atType;
  }

  public void setAtType(String atType) {
    this.atType = atType;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Sender sender = (Sender) o;
    return Objects.equals(this.id, sender.id) &&
        Objects.equals(this.email, sender.email) &&
        Objects.equals(this.name, sender.name) &&
        Objects.equals(this.phoneNumber, sender.phoneNumber) &&
        Objects.equals(this.party, sender.party) &&
        Objects.equals(this.atBaseType, sender.atBaseType) &&
        Objects.equals(this.atSchemaLocation, sender.atSchemaLocation) &&
        Objects.equals(this.atType, sender.atType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, email, name, phoneNumber, party, atBaseType, atSchemaLocation, atType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Sender {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    phoneNumber: ").append(toIndentedString(phoneNumber)).append("\n");
    sb.append("    party: ").append(toIndentedString(party)).append("\n");
    sb.append("    atBaseType: ").append(toIndentedString(atBaseType)).append("\n");
    sb.append("    atSchemaLocation: ").append(toIndentedString(atSchemaLocation)).append("\n");
    sb.append("    atType: ").append(toIndentedString(atType)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

