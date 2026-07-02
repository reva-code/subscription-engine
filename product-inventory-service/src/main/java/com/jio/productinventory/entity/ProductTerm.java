package com.jio.productinventory.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

/** TMF637 §ProductTerm — contractual commitment (e.g. 24-month lock-in). */
@Entity
@Table(name = "product_term")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ProductTerm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "name", length = 255)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "duration_amount", precision = 10, scale = 2)
    private BigDecimal durationAmount;

    @Column(name = "duration_units", length = 50)
    private String durationUnits;

    @Column(name = "valid_for_start")
    private OffsetDateTime validForStart;

    @Column(name = "valid_for_end")
    private OffsetDateTime validForEnd;

    @Column(name = "at_type", length = 255)
    private String atType;
}
