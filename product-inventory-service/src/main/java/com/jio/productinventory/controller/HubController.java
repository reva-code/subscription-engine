package com.jio.productinventory.controller;

import com.jio.productinventory.dto.EventSubscriptionDto;
import com.jio.productinventory.service.EventSubscriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

/**
 * TMF637 §hub — event listener registration (POST /hub, DELETE /hub/{id}).
 */
@RestController
@RequestMapping("/tmf-api/productInventoryManagement/v4")
@RequiredArgsConstructor
@Tag(name = "hub", description = "TMF637 Event Hub — register/unregister listeners")
public class HubController {

    private final EventSubscriptionService subscriptionService;

    @PostMapping("/hub")
    @Operation(summary = "Register a listener")
    public ResponseEntity<EventSubscriptionDto> register(
            @Valid @RequestBody EventSubscriptionDto dto) {

        EventSubscriptionDto created = subscriptionService.register(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @DeleteMapping("/hub/{id}")
    @Operation(summary = "Unregister a listener")
    public ResponseEntity<Void> unregister(@PathVariable String id) {
        subscriptionService.unregister(id);
        return ResponseEntity.noContent().build();
    }
}
