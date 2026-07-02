package com.jio.productinventory.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

/** TMF637 §ResourceRef — physical/logical resource realizing this product (→ TMF639). */
@Entity
@Table(name = "realizing_resource")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class RealizingResource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @NotBlank
    @Column(name = "resource_id", nullable = false, length = 36)
    private String resourceId;

    @Column(name = "href", length = 512)
    private String href;

    @Column(name = "name", length = 255)
    private String name;

    @Column(name = "value", length = 255)
    private String value;

    @Column(name = "at_type", length = 255)
    private String atType;

    @Column(name = "at_referred_type", length = 255)
    private String atReferredType;
}
