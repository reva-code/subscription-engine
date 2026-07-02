package com.jio.tmf622.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.jio.tmf622.model.CancelProductOrderStateChangeEventPayload;
import java.time.OffsetDateTime;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * The notification data structure
 */

@Schema(name = "CancelProductOrderStateChangeEvent", description = "The notification data structure")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-06-17T14:19:02.575985+05:30[Asia/Kolkata]", comments = "Generator version: 7.23.0")
public class CancelProductOrderStateChangeEvent {

  private @Nullable String id;

  private @Nullable String href;

  private @Nullable String eventId;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime eventTime;

  private @Nullable String eventType;

  private @Nullable String correlationId;

  private @Nullable String domain;

  private @Nullable String title;

  private @Nullable String description;

  private @Nullable String priority;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private @Nullable OffsetDateTime timeOcurred;

  private @Nullable CancelProductOrderStateChangeEventPayload event;

  public CancelProductOrderStateChangeEvent id(@Nullable String id) {
    this.id = id;
    return this;
  }

  /**
   * Identifier of the Process flow
   * @return id
   */
  
  @Schema(name = "id", description = "Identifier of the Process flow", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("id")
  public @Nullable String getId() {
    return id;
  }

  @JsonProperty("id")
  public void setId(@Nullable String id) {
    this.id = id;
  }

  public CancelProductOrderStateChangeEvent href(@Nullable String href) {
    this.href = href;
    return this;
  }

  /**
   * Reference of the ProcessFlow
   * @return href
   */
  
  @Schema(name = "href", description = "Reference of the ProcessFlow", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("href")
  public @Nullable String getHref() {
    return href;
  }

  @JsonProperty("href")
  public void setHref(@Nullable String href) {
    this.href = href;
  }

  public CancelProductOrderStateChangeEvent eventId(@Nullable String eventId) {
    this.eventId = eventId;
    return this;
  }

  /**
   * The identifier of the notification.
   * @return eventId
   */
  
  @Schema(name = "eventId", description = "The identifier of the notification.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("eventId")
  public @Nullable String getEventId() {
    return eventId;
  }

  @JsonProperty("eventId")
  public void setEventId(@Nullable String eventId) {
    this.eventId = eventId;
  }

  public CancelProductOrderStateChangeEvent eventTime(@Nullable OffsetDateTime eventTime) {
    this.eventTime = eventTime;
    return this;
  }

  /**
   * Time of the event occurrence.
   * @return eventTime
   */
  @Valid 
  @Schema(name = "eventTime", description = "Time of the event occurrence.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("eventTime")
  public @Nullable OffsetDateTime getEventTime() {
    return eventTime;
  }

  @JsonProperty("eventTime")
  public void setEventTime(@Nullable OffsetDateTime eventTime) {
    this.eventTime = eventTime;
  }

  public CancelProductOrderStateChangeEvent eventType(@Nullable String eventType) {
    this.eventType = eventType;
    return this;
  }

  /**
   * The type of the notification.
   * @return eventType
   */
  
  @Schema(name = "eventType", description = "The type of the notification.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("eventType")
  public @Nullable String getEventType() {
    return eventType;
  }

  @JsonProperty("eventType")
  public void setEventType(@Nullable String eventType) {
    this.eventType = eventType;
  }

  public CancelProductOrderStateChangeEvent correlationId(@Nullable String correlationId) {
    this.correlationId = correlationId;
    return this;
  }

  /**
   * The correlation id for this event.
   * @return correlationId
   */
  
  @Schema(name = "correlationId", description = "The correlation id for this event.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("correlationId")
  public @Nullable String getCorrelationId() {
    return correlationId;
  }

  @JsonProperty("correlationId")
  public void setCorrelationId(@Nullable String correlationId) {
    this.correlationId = correlationId;
  }

  public CancelProductOrderStateChangeEvent domain(@Nullable String domain) {
    this.domain = domain;
    return this;
  }

  /**
   * The domain of the event.
   * @return domain
   */
  
  @Schema(name = "domain", description = "The domain of the event.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("domain")
  public @Nullable String getDomain() {
    return domain;
  }

  @JsonProperty("domain")
  public void setDomain(@Nullable String domain) {
    this.domain = domain;
  }

  public CancelProductOrderStateChangeEvent title(@Nullable String title) {
    this.title = title;
    return this;
  }

  /**
   * The title of the event.
   * @return title
   */
  
  @Schema(name = "title", description = "The title of the event.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("title")
  public @Nullable String getTitle() {
    return title;
  }

  @JsonProperty("title")
  public void setTitle(@Nullable String title) {
    this.title = title;
  }

  public CancelProductOrderStateChangeEvent description(@Nullable String description) {
    this.description = description;
    return this;
  }

  /**
   * An explnatory of the event.
   * @return description
   */
  
  @Schema(name = "description", description = "An explnatory of the event.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("description")
  public @Nullable String getDescription() {
    return description;
  }

  @JsonProperty("description")
  public void setDescription(@Nullable String description) {
    this.description = description;
  }

  public CancelProductOrderStateChangeEvent priority(@Nullable String priority) {
    this.priority = priority;
    return this;
  }

  /**
   * A priority.
   * @return priority
   */
  
  @Schema(name = "priority", description = "A priority.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("priority")
  public @Nullable String getPriority() {
    return priority;
  }

  @JsonProperty("priority")
  public void setPriority(@Nullable String priority) {
    this.priority = priority;
  }

  public CancelProductOrderStateChangeEvent timeOcurred(@Nullable OffsetDateTime timeOcurred) {
    this.timeOcurred = timeOcurred;
    return this;
  }

  /**
   * The time the event occured.
   * @return timeOcurred
   */
  @Valid 
  @Schema(name = "timeOcurred", description = "The time the event occured.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("timeOcurred")
  public @Nullable OffsetDateTime getTimeOcurred() {
    return timeOcurred;
  }

  @JsonProperty("timeOcurred")
  public void setTimeOcurred(@Nullable OffsetDateTime timeOcurred) {
    this.timeOcurred = timeOcurred;
  }

  public CancelProductOrderStateChangeEvent event(@Nullable CancelProductOrderStateChangeEventPayload event) {
    this.event = event;
    return this;
  }

  /**
   * Get event
   * @return event
   */
  @Valid 
  @Schema(name = "event", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("event")
  public @Nullable CancelProductOrderStateChangeEventPayload getEvent() {
    return event;
  }

  @JsonProperty("event")
  public void setEvent(@Nullable CancelProductOrderStateChangeEventPayload event) {
    this.event = event;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CancelProductOrderStateChangeEvent cancelProductOrderStateChangeEvent = (CancelProductOrderStateChangeEvent) o;
    return Objects.equals(this.id, cancelProductOrderStateChangeEvent.id) &&
        Objects.equals(this.href, cancelProductOrderStateChangeEvent.href) &&
        Objects.equals(this.eventId, cancelProductOrderStateChangeEvent.eventId) &&
        Objects.equals(this.eventTime, cancelProductOrderStateChangeEvent.eventTime) &&
        Objects.equals(this.eventType, cancelProductOrderStateChangeEvent.eventType) &&
        Objects.equals(this.correlationId, cancelProductOrderStateChangeEvent.correlationId) &&
        Objects.equals(this.domain, cancelProductOrderStateChangeEvent.domain) &&
        Objects.equals(this.title, cancelProductOrderStateChangeEvent.title) &&
        Objects.equals(this.description, cancelProductOrderStateChangeEvent.description) &&
        Objects.equals(this.priority, cancelProductOrderStateChangeEvent.priority) &&
        Objects.equals(this.timeOcurred, cancelProductOrderStateChangeEvent.timeOcurred) &&
        Objects.equals(this.event, cancelProductOrderStateChangeEvent.event);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, href, eventId, eventTime, eventType, correlationId, domain, title, description, priority, timeOcurred, event);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CancelProductOrderStateChangeEvent {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    href: ").append(toIndentedString(href)).append("\n");
    sb.append("    eventId: ").append(toIndentedString(eventId)).append("\n");
    sb.append("    eventTime: ").append(toIndentedString(eventTime)).append("\n");
    sb.append("    eventType: ").append(toIndentedString(eventType)).append("\n");
    sb.append("    correlationId: ").append(toIndentedString(correlationId)).append("\n");
    sb.append("    domain: ").append(toIndentedString(domain)).append("\n");
    sb.append("    title: ").append(toIndentedString(title)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    priority: ").append(toIndentedString(priority)).append("\n");
    sb.append("    timeOcurred: ").append(toIndentedString(timeOcurred)).append("\n");
    sb.append("    event: ").append(toIndentedString(event)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(@Nullable Object o) {
    return o == null ? "null" : o.toString().replace("\n", "\n    ");
  }
}

