package com.jio.productinventory.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.OffsetDateTime;

/** TMF637 §Note — free-text annotation on a product (CSR comments, audit remarks). */
@Entity
@Table(name = "product_note")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ProductNote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "author", length = 255)
    private String author;

    @Column(name = "date")
    private OffsetDateTime date;

    @NotBlank
    @Column(name = "text", columnDefinition = "TEXT", nullable = false)
    private String text;

    @Column(name = "at_type", length = 255)
    private String atType;
}
