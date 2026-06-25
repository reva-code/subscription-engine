package product_catalog_service.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "product_offering")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductOffering {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private String lifecycleStatus;

    @ManyToOne
    @JoinColumn(name = "product_specification_id")
    private ProductSpecification productSpecification;

    @OneToMany(mappedBy = "productOffering")
    @JsonManagedReference
    private List<ProductOfferingPrice> prices;

}