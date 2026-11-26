package com.jio.productinventory.service;

import com.jio.productinventory.dto.*;
import com.jio.productinventory.entity.*;
import com.jio.productinventory.events.ProductEventPublisher;
import com.jio.productinventory.exception.InvalidStatusTransitionException;
import com.jio.productinventory.exception.ProductNotFoundException;
import com.jio.productinventory.mapper.ProductMapper;
import com.jio.productinventory.repository.ProductRepository;
import com.jio.productinventory.util.HrefBuilder;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import com.jio.productinventory.repository.ProductStatusHistoryRepository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock private ProductRepository productRepository;
    @Mock private ProductStatusHistoryRepository productStatusHistoryRepository;
    @Mock private ProductMapper mapper;
    @Mock private ProductEventPublisher eventPublisher;
    @Mock private HrefBuilder hrefBuilder;

    @InjectMocks
    private ProductService service;

    @Test
    void createProduct_happyPath_returnsDto() {
        ProductCreateDto dto = ProductCreateDto.builder()
                .status("created")
                .name("Jio 5G Unlimited")
                .build();

        Product entity = Product.builder()
                .status(ProductStatusType.created)
                .name("Jio 5G Unlimited")
                .build();
        entity.setId("test-id");

        ProductDto expected = ProductDto.builder().id("test-id").name("Jio 5G Unlimited").build();

        when(mapper.toEntity(dto)).thenReturn(entity);
        when(hrefBuilder.buildProductHref(any())).thenReturn("http://localhost:8084/tmf-api/productInventoryManagement/v4/product/test-id");
        when(productRepository.save(any(Product.class))).thenReturn(entity);
        when(mapper.toDto(entity)).thenReturn(expected);

        ProductDto result = service.createProduct(dto);

        assertThat(result.getName()).isEqualTo("Jio 5G Unlimited");
        verify(eventPublisher).publishCreate(expected);
    }

    @Test
    void createProduct_invalidStatus_throwsIllegalArgument() {
        ProductCreateDto dto = ProductCreateDto.builder()
                .status("INVALID_STATUS")
                .build();

        assertThatThrownBy(() -> service.createProduct(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid status value");
    }

    @Test
    void getProduct_notFound_throwsProductNotFoundException() {
        when(productRepository.findByIdWithCollections("missing-id"))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getProduct("missing-id", null))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessageContaining("missing-id");
    }

    @Test
    void patchProduct_invalidStatusTransition_throwsException() {
        Product entity = Product.builder()
                .status(ProductStatusType.terminated)
                .build();
        entity.setId("id-1");

        when(productRepository.findByIdWithCollections("id-1"))
                .thenReturn(Optional.of(entity));

        ProductUpdateDto dto = ProductUpdateDto.builder().status("active").build();

        assertThatThrownBy(() -> service.patchProduct("id-1", dto))
                .isInstanceOf(InvalidStatusTransitionException.class)
                .hasMessageContaining("terminated");
    }

    @Test
    void deleteProduct_notFound_throwsException() {
        when(productRepository.findById("missing")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.deleteProduct("missing"))
                .isInstanceOf(ProductNotFoundException.class);
    }

    // ── getStatusHistory ───────────────────────────────────────

    @Test
    void getStatusHistory_validAuth_returnsOrderedHistory() {
        Product product = Product.builder()
                .relatedParty(List.of(RelatedParty.builder()
                        .partyId("cust-001")
                        .role("Customer")
                        .build()))
                .build();
        product.setId("id-1");

        OffsetDateTime t1 = OffsetDateTime.parse("2024-01-01T00:00:00Z");
        OffsetDateTime t2 = OffsetDateTime.parse("2024-02-01T00:00:00Z");
        ProductStatusHistory h1 = ProductStatusHistory.builder()
                .fromStatus(null).toStatus("created").reason("Product created").changedAt(t1).build();
        ProductStatusHistory h2 = ProductStatusHistory.builder()
                .fromStatus("created").toStatus("active").reason("Activated").changedAt(t2).build();

        when(productRepository.findByIdWithCollections("id-1")).thenReturn(Optional.of(product));
        when(productStatusHistoryRepository.findByProductIdOrderByChangedAtAsc("id-1"))
                .thenReturn(List.of(h1, h2));

        List<ProductStatusHistoryDto> result = service.getStatusHistory("id-1", "cust-001");

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getToStatus()).isEqualTo("created");
        assertThat(result.get(0).getChangedAt()).isEqualTo(t1);
        assertThat(result.get(1).getFromStatus()).isEqualTo("created");
        assertThat(result.get(1).getToStatus()).isEqualTo("active");
        assertThat(result.get(1).getChangedAt()).isEqualTo(t2);
    }

    @Test
    void getStatusHistory_accessDenied_throwsProductNotFoundException() {
        Product product = Product.builder()
                .relatedParty(List.of(RelatedParty.builder()
                        .partyId("cust-A")
                        .role("Customer")
                        .build()))
                .build();
        product.setId("id-1");

        when(productRepository.findByIdWithCollections("id-1")).thenReturn(Optional.of(product));

        assertThatThrownBy(() -> service.getStatusHistory("id-1", "cust-B"))
                .isInstanceOf(ProductNotFoundException.class);
    }

    @Test
    void getStatusHistory_notFound_throwsProductNotFoundException() {
        when(productRepository.findByIdWithCollections("missing-id")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getStatusHistory("missing-id", "cust-001"))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessageContaining("missing-id");
    }

    @Test
    void getStatusHistory_emptyHistory_returnsEmptyList() {
        Product product = Product.builder()
                .relatedParty(List.of(RelatedParty.builder()
                        .partyId("cust-001")
                        .role("Customer")
                        .build()))
                .build();
        product.setId("id-1");

        when(productRepository.findByIdWithCollections("id-1")).thenReturn(Optional.of(product));
        when(productStatusHistoryRepository.findByProductIdOrderByChangedAtAsc("id-1"))
                .thenReturn(List.of());

        List<ProductStatusHistoryDto> result = service.getStatusHistory("id-1", "cust-001");

        assertThat(result).isEmpty();
    }
}
