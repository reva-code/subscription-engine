package com.jio.productinventory.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.OffsetDateTime;

/** TMF637 §EventSubscription — hub listener registration. */
@Entity
@Table(name = "event_subscription")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class EventSubscription {

    @Id
    @Column(name = "id", length = 36, nullable = false)
    private String id;

    @NotBlank
    @Column(name = "callback", nullable = false, length = 1024)
    private String callback;

    @Column(name = "query", length = 2048)
    private String query;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;
}
