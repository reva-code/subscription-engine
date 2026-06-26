package com.jio.productinventory.service;

import com.jio.productinventory.dto.EventSubscriptionDto;
import com.jio.productinventory.entity.EventSubscription;
import com.jio.productinventory.exception.ProductNotFoundException;
import com.jio.productinventory.repository.EventSubscriptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class EventSubscriptionService {

    private final EventSubscriptionRepository repo;

    public EventSubscriptionDto register(EventSubscriptionDto dto) {
        EventSubscription sub = EventSubscription.builder()
                .id(UUID.randomUUID().toString())
                .callback(dto.getCallback())
                .query(dto.getQuery())
                .createdAt(OffsetDateTime.now())
                .build();
        repo.save(sub);
        log.info("Registered event subscription: id={} callback={}", sub.getId(), sub.getCallback());
        return EventSubscriptionDto.builder()
                .id(sub.getId())
                .callback(sub.getCallback())
                .query(sub.getQuery())
                .build();
    }

    public void unregister(String id) {
        if (!repo.existsById(id)) {
            throw new ProductNotFoundException("EventSubscription not found: " + id);
        }
        repo.deleteById(id);
        log.info("Unregistered event subscription: id={}", id);
    }
}
