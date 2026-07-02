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

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock private ProductRepository productRepository;
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
}
