package com.jio.tmf622.repository;

import com.jio.tmf622.entity.ProductOrderEntity;
import com.jio.tmf622.model.ProductOrderStateType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// @Repository — marks this interface as a Spring-managed bean.
// Spring Data JPA generates a real implementation at startup.
// You never write that implementation yourself.
//
// JpaRepository<ProductOrderEntity, String>
//   ├── ProductOrderEntity → which @Entity class (maps to product_order table)
//   └── String             → the type of the @Id field in that entity
//
// Inherited methods you get for free (no code needed):
//   save(entity)         → INSERT or UPDATE
//   findById(id)         → SELECT ... WHERE id = ?   returns Optional<ProductOrderEntity>
//   findAll()            → SELECT * FROM product_order
//   deleteById(id)       → DELETE WHERE id = ?
//   existsById(id)       → SELECT COUNT(*) WHERE id = ?
//   count()              → SELECT COUNT(*) FROM product_order
@Repository
public interface ProductOrderRepository extends JpaRepository<ProductOrderEntity, String> {

    // Spring Data JPA reads this method name and generates the SQL automatically.
    // "findBy" + "State" → WHERE state = ?
    // No @Query annotation needed. No SQL string. One line.
    List<ProductOrderEntity> findByState(ProductOrderStateType state);

}
