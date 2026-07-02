package com.jio.productinventory.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

/**
 * TMF637 §ProductRelationship.
 * Common relationshipTypes in Jio context:
 *   bundled     — child product inside a bundle
 *   reliesOn    — OTT add-on relies on base mobile plan
 *   targets     — product that this product is a migration target for
 *   isContainedIn — reverse of bundled
 */
@Entity
@Table(name = "product_relationship")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRelationship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @NotBlank
    @Column(name = "relationship_type", nullable = false, length = 100)
    private String relationshipType;

    @NotBlank
    @Column(name = "related_product_id", nullable = false, length = 36)
    private String relatedProductId;

    @Column(name = "related_product_href", length = 512)
    private String relatedProductHref;

    @Column(name = "related_product_name", length = 255)
    private String relatedProductName;

    @Column(name = "at_type", length = 255)
    private String atType;
}
