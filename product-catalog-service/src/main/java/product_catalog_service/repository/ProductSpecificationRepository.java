package product_catalog_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import product_catalog_service.entity.ProductSpecification;

public interface ProductSpecificationRepository
        extends JpaRepository<ProductSpecification, Long> {

}