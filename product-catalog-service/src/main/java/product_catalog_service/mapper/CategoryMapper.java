package product_catalog_service.mapper;

import product_catalog_service.dto.CategoryDTO;
import product_catalog_service.entity.Category;

public class CategoryMapper {

    public static CategoryDTO toDTO(Category entity) {
        return CategoryDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .build();
    }

    public static Category toEntity(CategoryDTO dto) {
        return Category.builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .build();
    }
}