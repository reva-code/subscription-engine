package product_catalog_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import product_catalog_service.entity.ProductOfferingPrice;
import product_catalog_service.service.ProductOfferingPriceService;

import java.util.List;

@RestController
@RequestMapping("/tmf-api/productCatalogManagement/v4/productOfferingPrices")
public class ProductOfferingPriceController {

    @Autowired
    private ProductOfferingPriceService service;

    @PostMapping
    public ProductOfferingPrice create(@RequestBody ProductOfferingPrice price) {
        return service.create(price);
    }

    @GetMapping
    public List<ProductOfferingPrice> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ProductOfferingPrice getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PutMapping("/{id}")
    public ProductOfferingPrice update(@PathVariable Long id,
                                       @RequestBody ProductOfferingPrice price) {
        return service.update(id, price);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}