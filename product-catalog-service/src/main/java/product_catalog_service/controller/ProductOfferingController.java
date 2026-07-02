package product_catalog_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import product_catalog_service.entity.ProductOffering;
import product_catalog_service.service.ProductOfferingService;

import java.util.List;

@RestController
@RequestMapping("/tmf-api/productCatalogManagement/v4/productOfferings")
public class ProductOfferingController {

    @Autowired
    private ProductOfferingService service;

    @PostMapping
    public ProductOffering create(@RequestBody ProductOffering offering) {
        return service.create(offering);
    }

    @GetMapping
    public List<ProductOffering> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ProductOffering getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PutMapping("/{id}")
    public ProductOffering update(@PathVariable Long id,
                                  @RequestBody ProductOffering offering) {
        return service.update(id, offering);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}