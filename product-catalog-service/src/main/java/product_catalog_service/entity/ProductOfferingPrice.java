package product_catalog_service.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product_offering_price")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductOfferingPrice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Double price;

    private String currency;

    private String recurringChargePeriod;

    @ManyToOne
    @JoinColumn(name = "product_offering_id")
    @JsonBackReference
    private ProductOffering productOffering;

}