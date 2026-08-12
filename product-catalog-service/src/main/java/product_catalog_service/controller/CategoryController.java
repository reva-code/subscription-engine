package product_catalog_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import product_catalog_service.entity.Category;
import product_catalog_service.service.CategoryService;

import java.util.List;

@RestController
@RequestMapping("/tmf-api/productCatalogManagement/v4/categories")
public class CategoryController {

    @Autowired
    private CategoryService service;

    @PostMapping
    public Category create(@RequestBody Category category) {
        return service.create(category);
    }

    @GetMapping
    public List<Category> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Category getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PutMapping("/{id}")
    public Category update(@PathVariable Long id,
                           @RequestBody Category category) {
        return service.update(id, category);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}