package com.jio.productinventory.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

/**
 * TMF637 §Characteristic — name/value pair attached to a Product.
 * Telecom examples: MSISDN, ICCID, planSpeed, dataLimit, voiceAllowance.
 * value is TEXT to accommodate structured values (JSON strings) with valueType discriminator.
 */
@Entity
@Table(name = "product_characteristic")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductCharacteristic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @NotBlank
    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "value", columnDefinition = "TEXT")
    private String value;

    @Column(name = "value_type", length = 100)
    private String valueType;

    @Column(name = "at_type", length = 255)
    private String atType;
}
