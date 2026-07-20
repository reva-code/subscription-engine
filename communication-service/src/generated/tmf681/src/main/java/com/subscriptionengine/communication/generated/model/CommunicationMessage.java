package com.subscriptionengine.communication.generated.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.subscriptionengine.communication.generated.model.Attachment;
import com.subscriptionengine.communication.generated.model.Characteristic;
import com.subscriptionengine.communication.generated.model.CommunicationMessageStateType;
import com.subscriptionengine.communication.generated.model.Receiver;
import com.subscriptionengine.communication.generated.model.Sender;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * Communication message means a notification approach in the format of a message which can be dispatched (sent) to the certain user by the system with the content which can be felt and understood by the recipient. The user can be either a final customer or a customer service agent. The message can reach the customer in different interaction channels, including: email, short message, mobile app notification (push).
 */

@Schema(name = "CommunicationMessage", description = "Communication message means a notification approach in the format of a message which can be dispatched (sent) to the certain user by the system with the content which can be felt and understood by the recipient. The user can be either a final customer or a customer service agent. The message can reach the customer in different interaction channels, including: email, short message, mobile app notification (push).")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-07-20T15:01:51.572067+05:30[Asia/Calcutta]", comments = "Generator version: 7.7.0")
public class CommunicationMessage {

  private String id;

  private String href;

  private String content;

  private String description;

  private Boolean logFlag;

  private String messageType;

  private String priority;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime scheduledSendTime;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime sendTime;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime sendTimeComplete;

  private String subject;

  private Integer tryTimes;

  @Valid
  private List<@Valid Attachment> attachment = new ArrayList<>();

  @Valid
  private List<@Valid Characteristic> characteristic = new ArrayList<>();

  @Valid
  private List<@Valid Receiver> receiver = new ArrayList<>();

  private Sender sender;

  private CommunicationMessageStateType state;

  private String communicationType;

  private String receiverMobile;

  private String messageBody;

  private String atBaseType;

  private URI atSchemaLocation;

  private String atType;

  public CommunicationMessage id(String id) {
    this.id = id;
    return this;
  }

  /**
   * Unique identifier of Communication Message
   * @return id
   */
  
  @Schema(name = "id", description = "Unique identifier of Communication Message", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("id")
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public CommunicationMessage href(String href) {
    this.href = href;
    return this;
  }

  /**
   * Hypertext Reference of the Communication Message
   * @return href
   */
  
  @Schema(name = "href", description = "Hypertext Reference of the Communication Message", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("href")
  public String getHref() {
    return href;
  }

  public void setHref(String href) {
    this.href = href;
  }

  public CommunicationMessage content(String content) {
    this.content = content;
    return this;
  }

  /**
   * The content of the communication message
   * @return content
   */
  
  @Schema(name = "content", description = "The content of the communication message", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("content")
  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public CommunicationMessage description(String description) {
    this.description = description;
    return this;
  }

  /**
   * Description for the whole object
   * @return description
   */
  
  @Schema(name = "description", description = "Description for the whole object", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("description")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public CommunicationMessage logFlag(Boolean logFlag) {
    this.logFlag = logFlag;
    return this;
  }

  /**
   * Get logFlag
   * @return logFlag
   */
  
  @Schema(name = "logFlag", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("logFlag")
  public Boolean getLogFlag() {
    return logFlag;
  }

  public void setLogFlag(Boolean logFlag) {
    this.logFlag = logFlag;
  }

  public CommunicationMessage messageType(String messageType) {
    this.messageType = messageType;
    return this;
  }

  /**
   * The type of message, such as: SMS, Email, Mobile app push notification
   * @return messageType
   */
  
  @Schema(name = "messageType", description = "The type of message, such as: SMS, Email, Mobile app push notification", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("messageType")
  public String getMessageType() {
    return messageType;
  }

  public void setMessageType(String messageType) {
    this.messageType = messageType;
  }

  public CommunicationMessage priority(String priority) {
    this.priority = priority;
    return this;
  }

  /**
   * The priority of the communication message. Small number means higher priority.
   * @return priority
   */
  
  @Schema(name = "priority", description = "The priority of the communication message. Small number means higher priority.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("priority")
  public String getPriority() {
    return priority;
  }

  public void setPriority(String priority) {
    this.priority = priority;
  }

  public CommunicationMessage scheduledSendTime(OffsetDateTime scheduledSendTime) {
    this.scheduledSendTime = scheduledSendTime;
    return this;
  }

  /**
   * The scheduled time for sending the communication message
   * @return scheduledSendTime
   */
  @Valid 
  @Schema(name = "scheduledSendTime", description = "The scheduled time for sending the communication message", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("scheduledSendTime")
  public OffsetDateTime getScheduledSendTime() {
    return scheduledSendTime;
  }

  public void setScheduledSendTime(OffsetDateTime scheduledSendTime) {
    this.scheduledSendTime = scheduledSendTime;
  }

  public CommunicationMessage sendTime(OffsetDateTime sendTime) {
    this.sendTime = sendTime;
    return this;
  }

  /**
   * The time of sending communication message
   * @return sendTime
   */
  @Valid 
  @Schema(name = "sendTime", description = "The time of sending communication message", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("sendTime")
  public OffsetDateTime getSendTime() {
    return sendTime;
  }

  public void setSendTime(OffsetDateTime sendTime) {
    this.sendTime = sendTime;
  }

  public CommunicationMessage sendTimeComplete(OffsetDateTime sendTimeComplete) {
    this.sendTimeComplete = sendTimeComplete;
    return this;
  }

  /**
   * The time of completion of sending communication message
   * @return sendTimeComplete
   */
  @Valid 
  @Schema(name = "sendTimeComplete", description = "The time of completion of sending communication message", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("sendTimeComplete")
  public OffsetDateTime getSendTimeComplete() {
    return sendTimeComplete;
  }

  public void setSendTimeComplete(OffsetDateTime sendTimeComplete) {
    this.sendTimeComplete = sendTimeComplete;
  }

  public CommunicationMessage subject(String subject) {
    this.subject = subject;
    return this;
  }

  /**
   * The title of the message, necessary for the email and mobile app push
   * @return subject
   */
  
  @Schema(name = "subject", description = "The title of the message, necessary for the email and mobile app push", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("subject")
  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public CommunicationMessage tryTimes(Integer tryTimes) {
    this.tryTimes = tryTimes;
    return this;
  }

  /**
   * How many times do you want to retry the delivery of this message?
   * @return tryTimes
   */
  
  @Schema(name = "tryTimes", description = "How many times do you want to retry the delivery of this message?", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("tryTimes")
  public Integer getTryTimes() {
    return tryTimes;
  }

  public void setTryTimes(Integer tryTimes) {
    this.tryTimes = tryTimes;
  }

  public CommunicationMessage attachment(List<@Valid Attachment> attachment) {
    this.attachment = attachment;
    return this;
  }

  public CommunicationMessage addAttachmentItem(Attachment attachmentItem) {
    if (this.attachment == null) {
      this.attachment = new ArrayList<>();
    }
    this.attachment.add(attachmentItem);
    return this;
  }

  /**
   * Any attachment associated with this message
   * @return attachment
   */
  @Valid 
  @Schema(name = "attachment", description = "Any attachment associated with this message", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("attachment")
  public List<@Valid Attachment> getAttachment() {
    return attachment;
  }

  public void setAttachment(List<@Valid Attachment> attachment) {
    this.attachment = attachment;
  }

  public CommunicationMessage characteristic(List<@Valid Characteristic> characteristic) {
    this.characteristic = characteristic;
    return this;
  }

  public CommunicationMessage addCharacteristicItem(Characteristic characteristicItem) {
    if (this.characteristic == null) {
      this.characteristic = new ArrayList<>();
    }
    this.characteristic.add(characteristicItem);
    return this;
  }

  /**
   * Any additional characteristic(s) of this message
   * @return characteristic
   */
  @Valid 
  @Schema(name = "characteristic", description = "Any additional characteristic(s) of this message", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("characteristic")
  public List<@Valid Characteristic> getCharacteristic() {
    return characteristic;
  }

  public void setCharacteristic(List<@Valid Characteristic> characteristic) {
    this.characteristic = characteristic;
  }

  public CommunicationMessage receiver(List<@Valid Receiver> receiver) {
    this.receiver = receiver;
    return this;
  }

  public CommunicationMessage addReceiverItem(Receiver receiverItem) {
    if (this.receiver == null) {
      this.receiver = new ArrayList<>();
    }
    this.receiver.add(receiverItem);
    return this;
  }

  /**
   * The receiver(s) of this message
   * @return receiver
   */
  @Valid 
  @Schema(name = "receiver", description = "The receiver(s) of this message", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("receiver")
  public List<@Valid Receiver> getReceiver() {
    return receiver;
  }

  public void setReceiver(List<@Valid Receiver> receiver) {
    this.receiver = receiver;
  }

  public CommunicationMessage sender(Sender sender) {
    this.sender = sender;
    return this;
  }

  /**
   * Get sender
   * @return sender
   */
  @Valid 
  @Schema(name = "sender", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("sender")
  public Sender getSender() {
    return sender;
  }

  public void setSender(Sender sender) {
    this.sender = sender;
  }

  public CommunicationMessage state(CommunicationMessageStateType state) {
    this.state = state;
    return this;
  }

  /**
   * Get state
   * @return state
   */
  @Valid 
  @Schema(name = "state", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("state")
  public CommunicationMessageStateType getState() {
    return state;
  }

  public void setState(CommunicationMessageStateType state) {
    this.state = state;
  }

  public CommunicationMessage communicationType(String communicationType) {
    this.communicationType = communicationType;
    return this;
  }

  /**
   * Communication channel: EMAIL, SMS, WHATSAPP
   * @return communicationType
   */
  
  @Schema(name = "communicationType", description = "Communication channel: EMAIL, SMS, WHATSAPP", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("communicationType")
  public String getCommunicationType() {
    return communicationType;
  }

  public void setCommunicationType(String communicationType) {
    this.communicationType = communicationType;
  }

  public CommunicationMessage receiverMobile(String receiverMobile) {
    this.receiverMobile = receiverMobile;
    return this;
  }

  /**
   * Mobile number of the receiver
   * @return receiverMobile
   */
  
  @Schema(name = "receiverMobile", description = "Mobile number of the receiver", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("receiverMobile")
  public String getReceiverMobile() {
    return receiverMobile;
  }

  public void setReceiverMobile(String receiverMobile) {
    this.receiverMobile = receiverMobile;
  }

  public CommunicationMessage messageBody(String messageBody) {
    this.messageBody = messageBody;
    return this;
  }

  /**
   * Content body of the message
   * @return messageBody
   */
  
  @Schema(name = "messageBody", description = "Content body of the message", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("messageBody")
  public String getMessageBody() {
    return messageBody;
  }

  public void setMessageBody(String messageBody) {
    this.messageBody = messageBody;
  }

  public CommunicationMessage atBaseType(String atBaseType) {
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

  public CommunicationMessage atSchemaLocation(URI atSchemaLocation) {
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

  public CommunicationMessage atType(String atType) {
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
    CommunicationMessage communicationMessage = (CommunicationMessage) o;
    return Objects.equals(this.id, communicationMessage.id) &&
        Objects.equals(this.href, communicationMessage.href) &&
        Objects.equals(this.content, communicationMessage.content) &&
        Objects.equals(this.description, communicationMessage.description) &&
        Objects.equals(this.logFlag, communicationMessage.logFlag) &&
        Objects.equals(this.messageType, communicationMessage.messageType) &&
        Objects.equals(this.priority, communicationMessage.priority) &&
        Objects.equals(this.scheduledSendTime, communicationMessage.scheduledSendTime) &&
        Objects.equals(this.sendTime, communicationMessage.sendTime) &&
        Objects.equals(this.sendTimeComplete, communicationMessage.sendTimeComplete) &&
        Objects.equals(this.subject, communicationMessage.subject) &&
        Objects.equals(this.tryTimes, communicationMessage.tryTimes) &&
        Objects.equals(this.attachment, communicationMessage.attachment) &&
        Objects.equals(this.characteristic, communicationMessage.characteristic) &&
        Objects.equals(this.receiver, communicationMessage.receiver) &&
        Objects.equals(this.sender, communicationMessage.sender) &&
        Objects.equals(this.state, communicationMessage.state) &&
        Objects.equals(this.communicationType, communicationMessage.communicationType) &&
        Objects.equals(this.receiverMobile, communicationMessage.receiverMobile) &&
        Objects.equals(this.messageBody, communicationMessage.messageBody) &&
        Objects.equals(this.atBaseType, communicationMessage.atBaseType) &&
        Objects.equals(this.atSchemaLocation, communicationMessage.atSchemaLocation) &&
        Objects.equals(this.atType, communicationMessage.atType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, href, content, description, logFlag, messageType, priority, scheduledSendTime, sendTime, sendTimeComplete, subject, tryTimes, attachment, characteristic, receiver, sender, state, communicationType, receiverMobile, messageBody, atBaseType, atSchemaLocation, atType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CommunicationMessage {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    href: ").append(toIndentedString(href)).append("\n");
    sb.append("    content: ").append(toIndentedString(content)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    logFlag: ").append(toIndentedString(logFlag)).append("\n");
    sb.append("    messageType: ").append(toIndentedString(messageType)).append("\n");
    sb.append("    priority: ").append(toIndentedString(priority)).append("\n");
    sb.append("    scheduledSendTime: ").append(toIndentedString(scheduledSendTime)).append("\n");
    sb.append("    sendTime: ").append(toIndentedString(sendTime)).append("\n");
    sb.append("    sendTimeComplete: ").append(toIndentedString(sendTimeComplete)).append("\n");
    sb.append("    subject: ").append(toIndentedString(subject)).append("\n");
    sb.append("    tryTimes: ").append(toIndentedString(tryTimes)).append("\n");
    sb.append("    attachment: ").append(toIndentedString(attachment)).append("\n");
    sb.append("    characteristic: ").append(toIndentedString(characteristic)).append("\n");
    sb.append("    receiver: ").append(toIndentedString(receiver)).append("\n");
    sb.append("    sender: ").append(toIndentedString(sender)).append("\n");
    sb.append("    state: ").append(toIndentedString(state)).append("\n");
    sb.append("    communicationType: ").append(toIndentedString(communicationType)).append("\n");
    sb.append("    receiverMobile: ").append(toIndentedString(receiverMobile)).append("\n");
    sb.append("    messageBody: ").append(toIndentedString(messageBody)).append("\n");
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

