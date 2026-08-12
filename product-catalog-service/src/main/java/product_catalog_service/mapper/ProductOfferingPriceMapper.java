package product_catalog_service.mapper;

import product_catalog_service.dto.ProductOfferingPriceDTO;
import product_catalog_service.entity.ProductOfferingPrice;

public class ProductOfferingPriceMapper {

    public static ProductOfferingPriceDTO toDTO(ProductOfferingPrice entity) {
        return ProductOfferingPriceDTO.builder()
                .id(entity.getId())
                .price(entity.getPrice())
                .currency(entity.getCurrency())
                .build();
    }

    public static ProductOfferingPrice toEntity(ProductOfferingPriceDTO dto) {
        return ProductOfferingPrice.builder()
                .id(dto.getId())
                .price(dto.getPrice())
                .currency(dto.getCurrency())
                .build();
    }
}