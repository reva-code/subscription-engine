package com.jio.productinventory.repository;

import com.jio.productinventory.entity.Product;
import com.jio.productinventory.entity.ProductStatusType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, String>, JpaSpecificationExecutor<Product> {

    // Full entity fetch with all collections (avoids N+1 for single GET)
    @Query("""
        SELECT p FROM Product p
        LEFT JOIN FETCH p.productCharacteristic
        LEFT JOIN FETCH p.relatedParty
        LEFT JOIN FETCH p.productPrice
        LEFT JOIN FETCH p.productTerm
        WHERE p.id = :id
        """)
    Optional<Product> findByIdWithCollections(@Param("id") String id);

    // Customer-centric: all products owned by a party
    @Query("""
        SELECT DISTINCT p FROM Product p
        JOIN p.relatedParty rp
        WHERE rp.partyId = :partyId AND rp.role = :role
        """)
    Page<Product> findByRelatedPartyIdAndRole(
            @Param("partyId") String partyId,
            @Param("role") String role,
            Pageable pageable);

    // Find by billing account (TMF666 Account Management link)
    Page<Product> findByBillingAccountId(String billingAccountId, Pageable pageable);

    // Find by status
    Page<Product> findByStatus(ProductStatusType status, Pageable pageable);

    // Find by product offering (TMF620 catalog link)
    Page<Product> findByProductOfferingId(String productOfferingId, Pageable pageable);

    // Reverse lookup: products created from a specific order
    @Query("""
        SELECT DISTINCT p FROM Product p
        JOIN p.productOrderItem oi
        WHERE oi.productOrderId = :orderId
        """)
    Page<Product> findByOrderId(@Param("orderId") String orderId, Pageable pageable);

    // Count by status (for metrics/reporting)
    long countByStatus(ProductStatusType status);

    // Existence check for product offering validation
    boolean existsByProductOfferingId(String productOfferingId);
}
