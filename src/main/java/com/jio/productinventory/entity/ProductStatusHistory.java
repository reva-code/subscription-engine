package com.jio.productinventory.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.OffsetDateTime;

/**
 * Audit trail of all product lifecycle transitions.
 * Not in TMF637 spec, but mandatory for telecom regulatory compliance.
 */
@Entity
@Table(name = "product_status_history")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ProductStatusHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "from_status", length = 50)
    private String fromStatus;

    @NotBlank
    @Column(name = "to_status", nullable = false, length = 50)
    private String toStatus;

    @Column(name = "reason", columnDefinition = "TEXT")
    private String reason;

    @Column(name = "changed_by", length = 100)
    private String changedBy;

    @Column(name = "changed_at", nullable = false)
    private OffsetDateTime changedAt;

    @Column(name = "order_ref_id", length = 36)
    private String orderRefId;
}
