package com.jio.productinventory.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

/** TMF637 §RelatedProductOrderItem — traceability link back to TMF622 order. */
@Entity
@Table(name = "product_order_ref")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ProductOrderRef {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @NotBlank
    @Column(name = "product_order_id", nullable = false, length = 36)
    private String productOrderId;

    @Column(name = "product_order_href", length = 512)
    private String productOrderHref;

    @NotBlank
    @Column(name = "order_item_id", nullable = false, length = 100)
    private String orderItemId;

    @Column(name = "order_item_action", length = 100)
    private String orderItemAction;

    @Column(name = "role", length = 100)
    private String role;

    @Column(name = "at_type", length = 255)
    private String atType;
}
