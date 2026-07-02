package com.jio.productinventory.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

/** TMF637 §RelatedPlaceRefOrValue — installation/service address for a product. */
@Entity
@Table(name = "product_place")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ProductPlace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "place_id", length = 36)
    private String placeId;

    @Column(name = "href", length = 512)
    private String href;

    @Column(name = "name", length = 255)
    private String name;

    @NotBlank
    @Column(name = "role", nullable = false, length = 100)
    private String role;

    @Column(name = "at_type", length = 255)
    private String atType;

    @Column(name = "at_referred_type", length = 255)
    private String atReferredType;
}
