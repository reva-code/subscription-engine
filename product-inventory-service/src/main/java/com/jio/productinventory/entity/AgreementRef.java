package com.jio.productinventory.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

/** TMF637 §AgreementItemRef — contract/agreement reference. */
@Entity
@Table(name = "agreement_ref")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AgreementRef {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @NotBlank
    @Column(name = "agreement_id", nullable = false, length = 36)
    private String agreementId;

    @Column(name = "href", length = 512)
    private String href;

    @Column(name = "name", length = 255)
    private String name;

    @Column(name = "agreement_item_id", length = 100)
    private String agreementItemId;

    @Column(name = "at_referred_type", length = 255)
    private String atReferredType;

    @Column(name = "at_type", length = 255)
    private String atType;
}
