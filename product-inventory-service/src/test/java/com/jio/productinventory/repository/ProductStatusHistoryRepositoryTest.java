package com.jio.productinventory.repository;

import com.jio.productinventory.entity.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.*;
import org.springframework.test.context.TestPropertySource;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

/**
 * Repository slice test using H2 in-memory database.
 * Validates the chronological status-history query.
 */
@DataJpaTest
@TestPropertySource(properties = {
    "spring.jpa.hibernate.ddl-auto=create-drop",
    "spring.flyway.enabled=false"
})
class ProductStatusHistoryRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductStatusHistoryRepository productStatusHistoryRepository;

    @Autowired
    private TestEntityManager em;

    private Product buildProduct(String name) {
        return Product.builder()
                .id(UUID.randomUUID().toString())
                .name(name)
                .status(ProductStatusType.active)
                .isBundle(false)
                .isCustomerVisible(true)
                .atType("Product")
                .atBaseType("Product")
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .build();
    }

    private ProductStatusHistory buildHistory(Product product, String from, String to, OffsetDateTime changedAt) {
        return ProductStatusHistory.builder()
                .product(product)
                .fromStatus(from)
                .toStatus(to)
                .reason("test transition")
                .changedAt(changedAt)
                .build();
    }

    @Test
    void findByProductIdOrderByChangedAtAsc_returnsChronologicalOrder() {
        Product product = productRepository.save(buildProduct("Jio Plan"));

        productStatusHistoryRepository.save(
                buildHistory(product, "active", "suspended", OffsetDateTime.parse("2024-03-01T00:00:00Z")));
        productStatusHistoryRepository.save(
                buildHistory(product, null, "created", OffsetDateTime.parse("2024-01-01T00:00:00Z")));
        productStatusHistoryRepository.save(
                buildHistory(product, "created", "active", OffsetDateTime.parse("2024-02-01T00:00:00Z")));
        em.flush();
        em.clear();

        List<ProductStatusHistory> history =
                productStatusHistoryRepository.findByProductIdOrderByChangedAtAsc(product.getId());

        assertThat(history).hasSize(3);
        assertThat(history.get(0).getToStatus()).isEqualTo("created");
        assertThat(history.get(1).getToStatus()).isEqualTo("active");
        assertThat(history.get(2).getToStatus()).isEqualTo("suspended");
    }

    @Test
    void findByProductIdOrderByChangedAtAsc_noHistory_returnsEmptyList() {
        Product product = productRepository.save(buildProduct("New Plan"));
        em.flush();
        em.clear();

        List<ProductStatusHistory> history =
                productStatusHistoryRepository.findByProductIdOrderByChangedAtAsc(product.getId());

        assertThat(history).isEmpty();
    }
}
