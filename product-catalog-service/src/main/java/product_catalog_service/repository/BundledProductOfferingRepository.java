package product_catalog_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import product_catalog_service.entity.BundledProductOffering;

public interface BundledProductOfferingRepository
        extends JpaRepository<BundledProductOffering, Long> {

}