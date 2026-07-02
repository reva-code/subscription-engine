package product_catalog_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import product_catalog_service.entity.BundledProductOffering;
import product_catalog_service.service.BundledProductOfferingService;

import java.util.List;

@RestController
@RequestMapping("/tmf-api/productCatalogManagement/v4/bundledProductOfferings")
public class BundledProductOfferingController {

    @Autowired
    private BundledProductOfferingService service;

    @PostMapping
    public BundledProductOffering create(@RequestBody BundledProductOffering bundle) {
        return service.create(bundle);
    }

    @GetMapping
    public List<BundledProductOffering> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public BundledProductOffering getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PutMapping("/{id}")
    public BundledProductOffering update(@PathVariable Long id,
                                         @RequestBody BundledProductOffering bundle) {
        return service.update(id, bundle);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}