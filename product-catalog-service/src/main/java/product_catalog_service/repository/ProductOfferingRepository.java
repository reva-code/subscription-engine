package product_catalog_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import product_catalog_service.entity.ProductOffering;

public interface ProductOfferingRepository
        extends JpaRepository<ProductOffering, Long> {

}