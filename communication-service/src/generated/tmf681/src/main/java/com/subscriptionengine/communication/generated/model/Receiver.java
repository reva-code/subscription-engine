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
 * Receivers of the communication message.
 */

@Schema(name = "Receiver", description = "Receivers of the communication message.")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-07-20T15:01:51.572067+05:30[Asia/Calcutta]", comments = "Generator version: 7.7.0")
public class Receiver {

  private String id;

  private String appUserId;

  private String email;

  private String ip;

  private String name;

  private String phoneNumber;

  private RelatedParty party;

  private String atBaseType;

  private URI atSchemaLocation;

  private String atType;

  public Receiver id(String id) {
    this.id = id;
    return this;
  }

  /**
   * ID of the receiver
   * @return id
   */
  
  @Schema(name = "id", description = "ID of the receiver", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("id")
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Receiver appUserId(String appUserId) {
    this.appUserId = appUserId;
    return this;
  }

  /**
   * ID of the mobile app user
   * @return appUserId
   */
  
  @Schema(name = "appUserId", description = "ID of the mobile app user", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("appUserId")
  public String getAppUserId() {
    return appUserId;
  }

  public void setAppUserId(String appUserId) {
    this.appUserId = appUserId;
  }

  public Receiver email(String email) {
    this.email = email;
    return this;
  }

  /**
   * Receiver address of email, if the communication type is email
   * @return email
   */
  
  @Schema(name = "email", description = "Receiver address of email, if the communication type is email", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("email")
  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Receiver ip(String ip) {
    this.ip = ip;
    return this;
  }

  /**
   * IP address of the receiver
   * @return ip
   */
  
  @Schema(name = "ip", description = "IP address of the receiver", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("ip")
  public String getIp() {
    return ip;
  }

  public void setIp(String ip) {
    this.ip = ip;
  }

  public Receiver name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Name of the receiver
   * @return name
   */
  
  @Schema(name = "name", description = "Name of the receiver", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Receiver phoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
    return this;
  }

  /**
   * Phone number of the receiver, if the communication type is SMS.
   * @return phoneNumber
   */
  
  @Schema(name = "phoneNumber", description = "Phone number of the receiver, if the communication type is SMS.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("phoneNumber")
  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public Receiver party(RelatedParty party) {
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

  public Receiver atBaseType(String atBaseType) {
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

  public Receiver atSchemaLocation(URI atSchemaLocation) {
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

  public Receiver atType(String atType) {
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
    Receiver receiver = (Receiver) o;
    return Objects.equals(this.id, receiver.id) &&
        Objects.equals(this.appUserId, receiver.appUserId) &&
        Objects.equals(this.email, receiver.email) &&
        Objects.equals(this.ip, receiver.ip) &&
        Objects.equals(this.name, receiver.name) &&
        Objects.equals(this.phoneNumber, receiver.phoneNumber) &&
        Objects.equals(this.party, receiver.party) &&
        Objects.equals(this.atBaseType, receiver.atBaseType) &&
        Objects.equals(this.atSchemaLocation, receiver.atSchemaLocation) &&
        Objects.equals(this.atType, receiver.atType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, appUserId, email, ip, name, phoneNumber, party, atBaseType, atSchemaLocation, atType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Receiver {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    appUserId: ").append(toIndentedString(appUserId)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    ip: ").append(toIndentedString(ip)).append("\n");
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

