package com.subscriptionengine.communication.controller;

import com.subscriptionengine.communication.dto.NotificationEvent;
import com.subscriptionengine.communication.service.CommunicationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/tmf-api/communicationManagement/v4")
public class EventController {

    private static final Logger log = LoggerFactory.getLogger(EventController.class);

    private final CommunicationService service;

    public EventController(CommunicationService service) {
        this.service = service;
    }

    @PostMapping("/events")
    public ResponseEntity<Void> receiveEvent(@Valid @RequestBody NotificationEvent event) {
        log.info("Received event type: {} with ID: {}", event.getEventType(), event.getEventId());
        try {
            service.processNotificationEvent(event);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } catch (Exception e) {
            log.error("Failed to process event. Error: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
