package product_catalog_service.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductOfferingDTO {

    private Long id;

    private String name;

    private String description;

    private String lifecycleStatus;

    private Long productSpecificationId;

}