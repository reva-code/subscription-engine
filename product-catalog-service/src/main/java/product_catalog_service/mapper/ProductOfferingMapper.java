package product_catalog_service.mapper;

import product_catalog_service.dto.ProductOfferingDTO;
import product_catalog_service.entity.ProductOffering;

public class ProductOfferingMapper {

    public static ProductOfferingDTO toDTO(ProductOffering entity) {
        return ProductOfferingDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .lifecycleStatus(entity.getLifecycleStatus())
                .build();
    }

    public static ProductOffering toEntity(ProductOfferingDTO dto) {
        return ProductOffering.builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .lifecycleStatus(dto.getLifecycleStatus())
                .build();
    }
}