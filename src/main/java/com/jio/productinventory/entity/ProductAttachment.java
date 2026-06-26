package com.jio.productinventory.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

/** TMF637 §AttachmentRefOrValue — document/contract attachment on a product. */
@Entity
@Table(name = "product_attachment")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ProductAttachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "attachment_id", length = 36)
    private String attachmentId;

    @Column(name = "href", length = 512)
    private String href;

    @Column(name = "name", length = 255)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "url", length = 1024)
    private String url;

    @Column(name = "mime_type", length = 100)
    private String mimeType;

    @Column(name = "size_amount", precision = 18, scale = 4)
    private BigDecimal sizeAmount;

    @Column(name = "size_units", length = 50)
    private String sizeUnits;

    @Column(name = "valid_for_start")
    private OffsetDateTime validForStart;

    @Column(name = "valid_for_end")
    private OffsetDateTime validForEnd;

    @Column(name = "at_type", length = 255)
    private String atType;

    @Column(name = "at_referred_type", length = 255)
    private String atReferredType;
}
