package product_catalog_service.mapper;

import product_catalog_service.dto.BundledProductOfferingDTO;
import product_catalog_service.entity.BundledProductOffering;

public class BundledProductOfferingMapper {

    public static BundledProductOfferingDTO toDTO(BundledProductOffering entity) {
        return BundledProductOfferingDTO.builder()
                .id(entity.getId())
                .bundleName(entity.getBundleName())
                .description(entity.getDescription())
                .build();
    }

    public static BundledProductOffering toEntity(BundledProductOfferingDTO dto) {
        return BundledProductOffering.builder()
                .id(dto.getId())
                .bundleName(dto.getBundleName())
                .description(dto.getDescription())
                .build();
    }
}