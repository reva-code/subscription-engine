package com.jio.productinventory.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

/** TMF637 §ServiceRef — service realizing this product (→ TMF638). */
@Entity
@Table(name = "realizing_service")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class RealizingService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @NotBlank
    @Column(name = "service_id", nullable = false, length = 36)
    private String serviceId;

    @Column(name = "href", length = 512)
    private String href;

    @Column(name = "name", length = 255)
    private String name;

    @Column(name = "at_type", length = 255)
    private String atType;

    @Column(name = "at_referred_type", length = 255)
    private String atReferredType;
}
