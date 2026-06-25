package product_catalog_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import product_catalog_service.entity.ProductOfferingPrice;

public interface ProductOfferingPriceRepository
        extends JpaRepository<ProductOfferingPrice, Long> {

}