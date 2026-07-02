package com.jio.productinventory.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jio.productinventory.dto.ProductDto;
import com.jio.productinventory.entity.EventSubscription;
import com.jio.productinventory.repository.EventSubscriptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Publishes TMF637 events to registered hub listeners via HTTP POST.
 * Async so event delivery never blocks the main transaction.
 *
 * Production extension point: replace RestTemplate with a Kafka producer
 * for guaranteed delivery. The interface stays the same.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ProductEventPublisher {

    private final EventSubscriptionRepository subscriptionRepo;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public static final String EVENT_CREATE        = "ProductCreateEvent";
    public static final String EVENT_ATTR_CHANGE   = "ProductAttributeValueChangeEvent";
    public static final String EVENT_STATE_CHANGE  = "ProductStateChangeEvent";
    public static final String EVENT_DELETE        = "ProductDeleteEvent";
    public static final String EVENT_BATCH         = "ProductBatchEvent";

    @Async
    public void publishCreate(ProductDto product) {
        publish(buildEvent(EVENT_CREATE, product));
    }

    @Async
    public void publishAttributeChange(ProductDto product) {
        publish(buildEvent(EVENT_ATTR_CHANGE, product));
    }

    @Async
    public void publishStateChange(ProductDto product) {
        publish(buildEvent(EVENT_STATE_CHANGE, product));
    }

    @Async
    public void publishDelete(ProductDto product) {
        publish(buildEvent(EVENT_DELETE, product));
    }

    private ProductEvent buildEvent(String eventType, ProductDto product) {
        return ProductEvent.builder()
                .id(UUID.randomUUID().toString())
                .eventId(UUID.randomUUID().toString())
                .eventTime(OffsetDateTime.now())
                .eventType(eventType)
                .domain("ProductInventory")
                .timeOccurred(OffsetDateTime.now())
                .event(ProductEvent.ProductEventPayload.builder()
                        .product(product)
                        .build())
                .build();
    }

    private void publish(ProductEvent event) {
        List<EventSubscription> subscriptions = subscriptionRepo.findAll();
        if (subscriptions.isEmpty()) return;

        for (EventSubscription sub : subscriptions) {
            try {
                if (matchesQuery(sub, event)) {
                    restTemplate.postForEntity(sub.getCallback(), event, Void.class);
                    log.debug("Event {} delivered to {}", event.getEventType(), sub.getCallback());
                }
            } catch (Exception e) {
                log.warn("Failed to deliver event {} to {}: {}",
                        event.getEventType(), sub.getCallback(), e.getMessage());
            }
        }
    }

    private boolean matchesQuery(EventSubscription sub, ProductEvent event) {
        // If no query filter, send all events
        if (sub.getQuery() == null || sub.getQuery().isBlank()) return true;
        // Simple eventType filter: query = "eventType=ProductCreateEvent"
        return sub.getQuery().contains(event.getEventType());
    }
}
