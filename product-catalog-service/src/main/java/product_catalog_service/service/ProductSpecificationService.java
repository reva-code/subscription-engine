package product_catalog_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import product_catalog_service.entity.ProductSpecification;
import product_catalog_service.repository.ProductSpecificationRepository;

import java.util.List;

@Service
public class ProductSpecificationService {

    @Autowired
    private ProductSpecificationRepository repository;

    // Create
    public ProductSpecification create(ProductSpecification specification) {
        return repository.save(specification);
    }

    // Get all
    public List<ProductSpecification> getAll() {
        return repository.findAll();
    }

    // Get by ID
    public ProductSpecification getById(Long id) {
        return repository.findById(id).orElse(null);
    }

    // Update
    public ProductSpecification update(Long id,
                                       ProductSpecification specification) {

        ProductSpecification existing =
                repository.findById(id).orElse(null);

        if (existing != null) {

            existing.setName(specification.getName());
            existing.setDescription(specification.getDescription());
            existing.setLifecycleStatus(
                    specification.getLifecycleStatus());

            return repository.save(existing);
        }

        return null;
    }

    // Delete
    public void delete(Long id) {
        repository.deleteById(id);
    }
}