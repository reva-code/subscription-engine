package com.jio.productinventory.service;

import com.jio.productinventory.client.CommunicationClient;
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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.List;
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
    @Mock private CommunicationClient communicationClient;

    @InjectMocks
    private ProductService service;

    @BeforeEach
    void setPauseWindow() {
        ReflectionTestUtils.setField(service, "pauseMinDays", 1);
        ReflectionTestUtils.setField(service, "pauseMaxDays", 90);
    }

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

    // ── PAUSE ────────────────────────────────────────────────

    private Product buildActiveProduct() {
        Product entity = Product.builder()
                .status(ProductStatusType.active)
                .name("Jio 5G Unlimited")
                .build();
        entity.setId("prod-1");
        RelatedParty customer = RelatedParty.builder()
                .partyId("cust-1")
                .role("Customer")
                .build();
        customer.setProduct(entity);
        entity.getRelatedParty().add(customer);
        return entity;
    }

    @Test
    void pauseProduct_fromActive_setsSuspendedAndPersistsResumeDateAndReason() {
        Product entity = buildActiveProduct();
        PauseProductRequestDto request = PauseProductRequestDto.builder()
                .resumeDate(LocalDate.now().plusDays(30))
                .reason("customer travel")
                .build();

        when(productRepository.findByIdWithCollections("prod-1")).thenReturn(Optional.of(entity));
        when(productRepository.save(any(Product.class))).thenAnswer(inv -> inv.getArgument(0));
        ProductDto expected = ProductDto.builder().id("prod-1").status("suspended").build();
        when(mapper.toDto(any(Product.class))).thenReturn(expected);

        ProductDto result = service.pauseProduct("prod-1", request);

        ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
        verify(productRepository).save(captor.capture());
        Product saved = captor.getValue();

        assertThat(saved.getStatus()).isEqualTo(ProductStatusType.suspended);
        assertThat(saved.getResumeDate()).isEqualTo(request.getResumeDate());
        assertThat(saved.getPauseReason()).isEqualTo("customer travel");
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void pauseProduct_createsStatusHistoryEntryWithReasonAndResumeDate() {
        Product entity = buildActiveProduct();
        LocalDate resumeDate = LocalDate.now().plusDays(15);
        PauseProductRequestDto request = PauseProductRequestDto.builder()
                .resumeDate(resumeDate)
                .reason("customer travel")
                .build();

        when(productRepository.findByIdWithCollections("prod-1")).thenReturn(Optional.of(entity));
        when(productRepository.save(any(Product.class))).thenAnswer(inv -> inv.getArgument(0));
        when(mapper.toDto(any(Product.class))).thenReturn(ProductDto.builder().id("prod-1").build());

        service.pauseProduct("prod-1", request);

        assertThat(entity.getStatusHistory()).hasSize(1);
        ProductStatusHistory history = entity.getStatusHistory().get(0);
        assertThat(history.getFromStatus()).isEqualTo("active");
        assertThat(history.getToStatus()).isEqualTo("suspended");
        assertThat(history.getReason()).isEqualTo("customer travel");
        assertThat(history.getResumeDate()).isEqualTo(resumeDate);
    }

    @Test
    void pauseProduct_publishesStateChangeEvent() {
        Product entity = buildActiveProduct();
        PauseProductRequestDto request = PauseProductRequestDto.builder()
                .resumeDate(LocalDate.now().plusDays(10))
                .reason("travel")
                .build();

        when(productRepository.findByIdWithCollections("prod-1")).thenReturn(Optional.of(entity));
        when(productRepository.save(any(Product.class))).thenAnswer(inv -> inv.getArgument(0));
        ProductDto expected = ProductDto.builder().id("prod-1").status("suspended").build();
        when(mapper.toDto(any(Product.class))).thenReturn(expected);

        service.pauseProduct("prod-1", request);

        verify(eventPublisher).publishStateChange(expected);
    }

    @Test
    void pauseProduct_sendsPauseSmsViaCommunicationService() {
        Product entity = buildActiveProduct();
        LocalDate resumeDate = LocalDate.now().plusDays(10);
        PauseProductRequestDto request = PauseProductRequestDto.builder()
                .resumeDate(resumeDate)
                .reason("travel")
                .build();

        when(productRepository.findByIdWithCollections("prod-1")).thenReturn(Optional.of(entity));
        when(productRepository.save(any(Product.class))).thenAnswer(inv -> inv.getArgument(0));
        when(mapper.toDto(any(Product.class))).thenReturn(ProductDto.builder().id("prod-1").build());

        service.pauseProduct("prod-1", request);

        ArgumentCaptor<String> bodyCaptor = ArgumentCaptor.forClass(String.class);
        verify(communicationClient).sendSms(eq("cust-1"), any(), any(), bodyCaptor.capture());
        assertThat(bodyCaptor.getValue()).contains(resumeDate.toString());
    }

    @Test
    void pauseProduct_alreadySuspended_throwsInvalidTransitionWithNoSideEffects() {
        Product entity = Product.builder().status(ProductStatusType.suspended).build();
        entity.setId("prod-1");

        when(productRepository.findByIdWithCollections("prod-1")).thenReturn(Optional.of(entity));

        PauseProductRequestDto request = PauseProductRequestDto.builder()
                .resumeDate(LocalDate.now().plusDays(10))
                .build();

        assertThatThrownBy(() -> service.pauseProduct("prod-1", request))
                .isInstanceOf(InvalidStatusTransitionException.class);

        verify(productRepository, never()).save(any());
        verify(eventPublisher, never()).publishStateChange(any());
        verify(communicationClient, never()).sendSms(any(), any(), any(), any());
    }

    @ParameterizedTest
    @EnumSource(value = ProductStatusType.class,
            names = {"cancelled", "terminated", "pendingActive", "pendingTerminate"})
    void pauseProduct_disallowedSourceStatuses_rejected(ProductStatusType status) {
        Product entity = Product.builder().status(status).build();
        entity.setId("prod-1");

        when(productRepository.findByIdWithCollections("prod-1")).thenReturn(Optional.of(entity));

        PauseProductRequestDto request = PauseProductRequestDto.builder()
                .resumeDate(LocalDate.now().plusDays(10))
                .build();

        assertThatThrownBy(() -> service.pauseProduct("prod-1", request))
                .isInstanceOf(InvalidStatusTransitionException.class);
    }

    @Test
    void pauseProduct_resumeDateToday_rejected() {
        Product entity = buildActiveProduct();
        when(productRepository.findByIdWithCollections("prod-1")).thenReturn(Optional.of(entity));

        PauseProductRequestDto request = PauseProductRequestDto.builder()
                .resumeDate(LocalDate.now())
                .build();

        assertThatThrownBy(() -> service.pauseProduct("prod-1", request))
                .isInstanceOf(IllegalArgumentException.class);

        assertThat(entity.getStatus()).isEqualTo(ProductStatusType.active);
        verify(productRepository, never()).save(any());
    }

    @Test
    void pauseProduct_resumeDateBeyond90Days_rejected() {
        Product entity = buildActiveProduct();
        when(productRepository.findByIdWithCollections("prod-1")).thenReturn(Optional.of(entity));

        PauseProductRequestDto request = PauseProductRequestDto.builder()
                .resumeDate(LocalDate.now().plusDays(91))
                .build();

        assertThatThrownBy(() -> service.pauseProduct("prod-1", request))
                .isInstanceOf(IllegalArgumentException.class);

        assertThat(entity.getStatus()).isEqualTo(ProductStatusType.active);
        verify(productRepository, never()).save(any());
    }

    // ── AUTO-RESUME ──────────────────────────────────────────

    @Test
    void autoResumeExpiredPauses_resumesDueProducts() {
        Product due = Product.builder()
                .status(ProductStatusType.suspended)
                .resumeDate(LocalDate.now().minusDays(1))
                .build();
        due.setId("prod-due");

        Page<Product> page = new PageImpl<>(List.of(due), PageRequest.of(0, 100), 1);
        when(productRepository.findByStatus(eq(ProductStatusType.suspended), any(Pageable.class)))
                .thenReturn(page);
        when(productRepository.save(any(Product.class))).thenAnswer(inv -> inv.getArgument(0));
        ProductDto expected = ProductDto.builder().id("prod-due").status("active").build();
        when(mapper.toDto(any(Product.class))).thenReturn(expected);

        service.autoResumeExpiredPauses();

        ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
        verify(productRepository).save(captor.capture());
        assertThat(captor.getValue().getStatus()).isEqualTo(ProductStatusType.active);
        assertThat(captor.getValue().getStatusHistory()).hasSize(1);
        verify(eventPublisher).publishStateChange(expected);
    }

    @Test
    void autoResumeExpiredPauses_noElapsedResumeDates_doesNothing() {
        Product notDue = Product.builder()
                .status(ProductStatusType.suspended)
                .resumeDate(LocalDate.now().plusDays(5))
                .build();
        notDue.setId("prod-not-due");

        Page<Product> page = new PageImpl<>(List.of(notDue), PageRequest.of(0, 100), 1);
        when(productRepository.findByStatus(eq(ProductStatusType.suspended), any(Pageable.class)))
                .thenReturn(page);

        service.autoResumeExpiredPauses();

        verify(productRepository, never()).save(any());
        verify(eventPublisher, never()).publishStateChange(any());
    }
}
