package product_catalog_service.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import product_catalog_service.entity.ProductSpecification;
import product_catalog_service.repository.ProductSpecificationRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductSpecificationServiceTest {

    @Mock
    private ProductSpecificationRepository repository;

    @InjectMocks
    private ProductSpecificationService service;

    @Test
    void testCreateSpecification() {

        ProductSpecification specification = ProductSpecification.builder()
                .name("Netflix Premium")
                .description("4K Streaming")
                .lifecycleStatus("ACTIVE")
                .build();

        when(repository.save(specification)).thenReturn(specification);

        ProductSpecification saved = service.create(specification);

        assertEquals("Netflix Premium", saved.getName());

        verify(repository, times(1)).save(specification);
    }

    @Test
    void testGetAllSpecifications() {

        ProductSpecification specification = ProductSpecification.builder()
                .id(1L)
                .name("Netflix Premium")
                .description("4K Streaming")
                .lifecycleStatus("ACTIVE")
                .build();

        when(repository.findAll()).thenReturn(List.of(specification));

        List<ProductSpecification> result = service.getAll();

        assertEquals(1, result.size());
        assertEquals("Netflix Premium", result.get(0).getName());

        verify(repository).findAll();
    }

    @Test
    void testGetSpecificationById() {

        ProductSpecification specification = ProductSpecification.builder()
                .id(1L)
                .name("Netflix Premium")
                .description("4K Streaming")
                .lifecycleStatus("ACTIVE")
                .build();

        when(repository.findById(1L)).thenReturn(Optional.of(specification));

        ProductSpecification result = service.getById(1L);

        assertEquals("Netflix Premium", result.getName());

        verify(repository).findById(1L);
    }

    @Test
    void testUpdateSpecification() {

        ProductSpecification existing = ProductSpecification.builder()
                .id(1L)
                .name("Old")
                .description("Old Description")
                .lifecycleStatus("INACTIVE")
                .build();

        ProductSpecification updated = ProductSpecification.builder()
                .name("Netflix Premium")
                .description("4K Streaming")
                .lifecycleStatus("ACTIVE")
                .build();

        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(any(ProductSpecification.class))).thenReturn(existing);

        ProductSpecification result = service.update(1L, updated);

        assertEquals("Netflix Premium", result.getName());
        assertEquals("4K Streaming", result.getDescription());
        assertEquals("ACTIVE", result.getLifecycleStatus());

        verify(repository).save(existing);
    }

    @Test
    void testDeleteSpecification() {

        service.delete(1L);

        verify(repository).deleteById(1L);
    }
}