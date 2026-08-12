package product_catalog_service.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import product_catalog_service.entity.ProductOffering;
import product_catalog_service.repository.ProductOfferingRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductOfferingServiceTest {

    @Mock
    private ProductOfferingRepository repository;

    @InjectMocks
    private ProductOfferingService service;

    @Test
    void testCreateOffering() {

        ProductOffering offering = ProductOffering.builder()
                .name("Netflix Summer Offer")
                .description("Discount")
                .lifecycleStatus("ACTIVE")
                .build();

        when(repository.save(offering)).thenReturn(offering);

        ProductOffering saved = service.create(offering);

        assertEquals("Netflix Summer Offer", saved.getName());

        verify(repository).save(offering);
    }

    @Test
    void testGetAllOfferings() {

        ProductOffering offering = ProductOffering.builder()
                .id(1L)
                .name("Netflix Summer Offer")
                .description("Discount")
                .lifecycleStatus("ACTIVE")
                .build();

        when(repository.findAll()).thenReturn(List.of(offering));

        List<ProductOffering> result = service.getAll();

        assertEquals(1, result.size());
        assertEquals("Netflix Summer Offer", result.get(0).getName());

        verify(repository).findAll();
    }

    @Test
    void testGetOfferingById() {

        ProductOffering offering = ProductOffering.builder()
                .id(1L)
                .name("Netflix Summer Offer")
                .description("Discount")
                .lifecycleStatus("ACTIVE")
                .build();

        when(repository.findById(1L)).thenReturn(Optional.of(offering));

        ProductOffering result = service.getById(1L);

        assertEquals("Netflix Summer Offer", result.getName());

        verify(repository).findById(1L);
    }

    @Test
    void testUpdateOffering() {

        ProductOffering existing = ProductOffering.builder()
                .id(1L)
                .name("Old")
                .description("Old Description")
                .lifecycleStatus("INACTIVE")
                .build();

        ProductOffering updated = ProductOffering.builder()
                .name("Netflix Summer Offer")
                .description("Discount")
                .lifecycleStatus("ACTIVE")
                .build();

        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(any(ProductOffering.class))).thenReturn(existing);

        ProductOffering result = service.update(1L, updated);

        assertEquals("Netflix Summer Offer", result.getName());
        assertEquals("Discount", result.getDescription());
        assertEquals("ACTIVE", result.getLifecycleStatus());

        verify(repository).save(existing);
    }

    @Test
    void testDeleteOffering() {

        service.delete(1L);

        verify(repository).deleteById(1L);
    }
}