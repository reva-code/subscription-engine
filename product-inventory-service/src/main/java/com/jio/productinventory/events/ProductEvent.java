package com.jio.productinventory.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jio.productinventory.dto.ProductDto;
import lombok.*;

import java.time.OffsetDateTime;

/**
 * Base TMF637 event envelope.
 * All event types share the same structure; eventType discriminates.
 *
 * TMF event type names follow: {Resource}{EventKind}Event
 */
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ProductEvent {

    @JsonProperty("id")
    private String id;

    @JsonProperty("href")
    private String href;

    @JsonProperty("eventId")
    private String eventId;

    @JsonProperty("eventTime")
    private OffsetDateTime eventTime;

    @JsonProperty("eventType")
    private String eventType;

    @JsonProperty("correlationId")
    private String correlationId;

    @JsonProperty("domain")
    @Builder.Default
    private String domain = "ProductInventory";

    @JsonProperty("title")
    private String title;

    @JsonProperty("description")
    private String description;

    @JsonProperty("priority")
    private String priority;

    @JsonProperty("timeOccurred")
    private OffsetDateTime timeOccurred;

    @JsonProperty("event")
    private ProductEventPayload event;

    @Data @NoArgsConstructor @AllArgsConstructor @Builder
    public static class ProductEventPayload {
        @JsonProperty("product")
        private ProductDto product;
    }
}
