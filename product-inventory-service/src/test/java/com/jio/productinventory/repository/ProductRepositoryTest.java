package com.jio.productinventory.repository;

import com.jio.productinventory.entity.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

/**
 * Repository slice test using H2 in-memory database.
 * Validates JPA mappings, queries, and constraints.
 */
@DataJpaTest
@TestPropertySource(properties = {
    "spring.jpa.hibernate.ddl-auto=create-drop",
    "spring.flyway.enabled=false"
})
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TestEntityManager em;

    private Product buildProduct(String name, ProductStatusType status) {
        return Product.builder()
                .id(java.util.UUID.randomUUID().toString())
                .name(name)
                .status(status)
                .isBundle(false)
                .isCustomerVisible(true)
                .atType("Product")
                .atBaseType("Product")
                .build();
    }

    @Test
    void save_andFindById_succeeds() {
        Product p = buildProduct("Jio 5G Plan", ProductStatusType.active);
        productRepository.save(p);
        em.flush();
        em.clear();

        Optional<Product> found = productRepository.findById(p.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Jio 5G Plan");
        assertThat(found.get().getStatus()).isEqualTo(ProductStatusType.active);
    }

    @Test
    void findByStatus_returnsFilteredResults() {
        productRepository.save(buildProduct("Active Plan", ProductStatusType.active));
        productRepository.save(buildProduct("Suspended Plan", ProductStatusType.suspended));
        productRepository.save(buildProduct("Active OTT", ProductStatusType.active));
        em.flush(); em.clear();

        Page<Product> active = productRepository.findByStatus(
                ProductStatusType.active, PageRequest.of(0, 10));

        assertThat(active.getTotalElements()).isEqualTo(2);
        assertThat(active.getContent()).allMatch(p -> p.getStatus() == ProductStatusType.active);
    }

    @Test
    void findByBillingAccountId_returnsCorrectProducts() {
        Product p1 = buildProduct("Plan 1", ProductStatusType.active);
        p1.setBillingAccountId("BA-001");
        Product p2 = buildProduct("Plan 2", ProductStatusType.active);
        p2.setBillingAccountId("BA-002");
        productRepository.saveAll(java.util.List.of(p1, p2));
        em.flush(); em.clear();

        Page<Product> result = productRepository.findByBillingAccountId("BA-001", PageRequest.of(0, 10));
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).getName()).isEqualTo("Plan 1");
    }

    @Test
    void productWithCharacteristic_savedAndLoaded() {
        Product p = buildProduct("Broadband Plan", ProductStatusType.created);
        ProductCharacteristic char1 = ProductCharacteristic.builder()
                .name("downloadSpeed")
                .value("100")
                .valueType("string")
                .build();
        char1.setProduct(p);
        p.getProductCharacteristic().add(char1);
        productRepository.save(p);
        em.flush(); em.clear();

        Optional<Product> found = productRepository.findByIdWithCollections(p.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getProductCharacteristic()).hasSize(1);
        assertThat(found.get().getProductCharacteristic().get(0).getName()).isEqualTo("downloadSpeed");
    }

    @Test
    void countByStatus_returnsCorrectCount() {
        productRepository.save(buildProduct("P1", ProductStatusType.active));
        productRepository.save(buildProduct("P2", ProductStatusType.active));
        productRepository.save(buildProduct("P3", ProductStatusType.terminated));

        assertThat(productRepository.countByStatus(ProductStatusType.active)).isEqualTo(2);
        assertThat(productRepository.countByStatus(ProductStatusType.terminated)).isEqualTo(1);
    }
}
