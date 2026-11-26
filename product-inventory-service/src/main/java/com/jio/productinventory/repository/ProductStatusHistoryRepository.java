package com.jio.productinventory.repository;

import com.jio.productinventory.entity.ProductStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductStatusHistoryRepository extends JpaRepository<ProductStatusHistory, Long> {

    // Chronological status transitions for a given product
    List<ProductStatusHistory> findByProductIdOrderByChangedAtAsc(String productId);
}
