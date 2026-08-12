package product_catalog_service.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ProductOfferingPriceDTO {

    private Long id;

    private Double price;

    private String currency;

}