package product_catalog_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import product_catalog_service.entity.ProductSpecification;
import product_catalog_service.service.ProductSpecificationService;

import java.util.List;

@RestController
@RequestMapping("/tmf-api/productCatalogManagement/v4/productSpecifications")
public class ProductSpecificationController {

    @Autowired
    private ProductSpecificationService service;

    @PostMapping
    public ProductSpecification create(@RequestBody ProductSpecification specification) {
        return service.create(specification);
    }

    @GetMapping
    public List<ProductSpecification> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ProductSpecification getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PutMapping("/{id}")
    public ProductSpecification update(@PathVariable Long id,
                                       @RequestBody ProductSpecification specification) {
        return service.update(id, specification);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}