package com.jio.productinventory.controller;

import com.jio.productinventory.events.ProductEvent;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * TMF637 §listener — client-side notification endpoints.
 * These receive events FROM other services that this service subscribes to.
 * For example: Product Order service notifying this service of order completion.
 */
@RestController
@RequestMapping("/tmf-api/productInventoryManagement/v4/listener")
@Slf4j
@Tag(name = "listener", description = "TMF637 Notification Listeners")
public class ListenerController {

    @PostMapping("/productCreateEvent")
    @Operation(summary = "Receives ProductCreateEvent notifications")
    public ResponseEntity<Void> onProductCreate(@RequestBody ProductEvent event) {
        log.info("Received ProductCreateEvent: id={}", event.getEventId());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/productAttributeValueChangeEvent")
    @Operation(summary = "Receives ProductAttributeValueChangeEvent notifications")
    public ResponseEntity<Void> onAttributeChange(@RequestBody ProductEvent event) {
        log.info("Received ProductAttributeValueChangeEvent: id={}", event.getEventId());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/productStateChangeEvent")
    @Operation(summary = "Receives ProductStateChangeEvent notifications")
    public ResponseEntity<Void> onStateChange(@RequestBody ProductEvent event) {
        log.info("Received ProductStateChangeEvent: id={}", event.getEventId());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/productDeleteEvent")
    @Operation(summary = "Receives ProductDeleteEvent notifications")
    public ResponseEntity<Void> onDelete(@RequestBody ProductEvent event) {
        log.info("Received ProductDeleteEvent: id={}", event.getEventId());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/productBatchEvent")
    @Operation(summary = "Receives ProductBatchEvent notifications")
    public ResponseEntity<Void> onBatch(@RequestBody ProductEvent event) {
        log.info("Received ProductBatchEvent: id={}", event.getEventId());
        return ResponseEntity.noContent().build();
    }
}
