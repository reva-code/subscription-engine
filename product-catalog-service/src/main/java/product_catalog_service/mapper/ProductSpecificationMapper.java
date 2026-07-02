package product_catalog_service.mapper;

import product_catalog_service.dto.ProductSpecificationDTO;
import product_catalog_service.entity.ProductSpecification;

public class ProductSpecificationMapper {

    public static ProductSpecificationDTO toDTO(ProductSpecification entity) {
        return ProductSpecificationDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .lifecycleStatus(entity.getLifecycleStatus())
                .build();
    }

    public static ProductSpecification toEntity(ProductSpecificationDTO dto) {
        return ProductSpecification.builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .lifecycleStatus(dto.getLifecycleStatus())
                .build();
    }
}