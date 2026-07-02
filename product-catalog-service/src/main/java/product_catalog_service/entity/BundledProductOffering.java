package product_catalog_service.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "bundled_product_offering")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BundledProductOffering {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String bundleName;

    private String description;

    @ManyToOne
    @JoinColumn(name = "product_offering_id")
    private ProductOffering productOffering;

}