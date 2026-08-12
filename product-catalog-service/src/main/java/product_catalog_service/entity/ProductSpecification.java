package product_catalog_service.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product_specification")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductSpecification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private String lifecycleStatus;

}
