package product_catalog_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import product_catalog_service.entity.Category;
import product_catalog_service.repository.CategoryRepository;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository repository;

    // CREATE
    public Category create(Category category) {
        return repository.save(category);
    }

    // GET ALL
    public List<Category> getAll() {
        return repository.findAll();
    }

    // GET BY ID
    public Category getById(Long id) {
        return repository.findById(id).orElse(null);
    }

    // UPDATE
    public Category update(Long id, Category category) {

        Category existing = repository.findById(id).orElse(null);

        if (existing != null) {

            existing.setName(category.getName());
            existing.setDescription(category.getDescription());

            return repository.save(existing);
        }

        return null;
    }

    // DELETE
    public void delete(Long id) {
        repository.deleteById(id);
    }
}