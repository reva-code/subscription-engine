package product_catalog_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import product_catalog_service.entity.ProductOffering;
import product_catalog_service.repository.ProductOfferingRepository;

import java.util.List;

@Service
public class ProductOfferingService {

    @Autowired
    private ProductOfferingRepository repository;

    // CREATE
    public ProductOffering create(ProductOffering offering) {
        return repository.save(offering);
    }

    // GET ALL
    public List<ProductOffering> getAll() {
        return repository.findAll();
    }

    // GET BY ID
    public ProductOffering getById(Long id) {
        return repository.findById(id).orElse(null);
    }

    // UPDATE
    public ProductOffering update(Long id,
                                  ProductOffering offering) {

        ProductOffering existing =
                repository.findById(id).orElse(null);

        if (existing != null) {

            existing.setName(offering.getName());
            existing.setDescription(offering.getDescription());
            existing.setLifecycleStatus(
                    offering.getLifecycleStatus());

            return repository.save(existing);
        }

        return null;
    }

    // DELETE
    public void delete(Long id) {
        repository.deleteById(id);
    }
}