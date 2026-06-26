package com.jio.productinventory.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

/** TMF637 §RelatedParty — links Customer, Dealer, CSR to a product. */
@Entity
@Table(name = "related_party")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class RelatedParty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @NotBlank
    @Column(name = "party_id", nullable = false, length = 36)
    private String partyId;

    @Column(name = "href", length = 512)
    private String href;

    @Column(name = "name", length = 255)
    private String name;

    @NotBlank
    @Column(name = "role", nullable = false, length = 100)
    private String role;

    @Column(name = "at_referred_type", length = 255)
    private String atReferredType;

    @Column(name = "at_type", length = 255)
    private String atType;
}
