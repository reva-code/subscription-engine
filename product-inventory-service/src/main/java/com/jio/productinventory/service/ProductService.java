package com.jio.productinventory.service;

import com.jio.productinventory.dto.*;
import com.jio.productinventory.entity.*;
import com.jio.productinventory.events.ProductEventPublisher;
import com.jio.productinventory.exception.ProductNotFoundException;
import com.jio.productinventory.mapper.ProductMapper;
import com.jio.productinventory.repository.ProductRepository;
import com.jio.productinventory.repository.ProductSpecification;
import com.jio.productinventory.util.HrefBuilder;
import com.jio.productinventory.validation.ProductStatusMachine;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper mapper;
    private final ProductEventPublisher eventPublisher;
    private final HrefBuilder hrefBuilder;

    // ── CREATE ──────────────────────────────────────────────

    public ProductDto createProduct(ProductCreateDto dto) {
        log.info("Creating product with status={} offering={}",
                dto.getStatus(), dto.getProductOffering() != null ? dto.getProductOffering().getId() : null);

        // Validate status value
        ProductStatusType status = parseStatus(dto.getStatus());

        Product product = mapper.toEntity(dto);
        product.setId(UUID.randomUUID().toString());
        product.setStatus(status);
        product.setHref(hrefBuilder.buildProductHref(product.getId()));

        // Wire up collections with back-references
        populateCollections(product, dto);

        // Record initial status
        appendStatusHistory(product, null, status, "Product created", null);

        Product saved = productRepository.save(product);
        ProductDto result = mapper.toDto(saved);

        eventPublisher.publishCreate(result);
        log.info("Product created: id={}", saved.getId());
        return result;
    }

    // ── READ ONE ─────────────────────────────────────────────

    @Transactional(readOnly = true)
    public ProductDto getProduct(String id, String fields) {
        Product product = productRepository.findByIdWithCollections(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        ProductDto dto = mapper.toDto(product);
        if (fields != null && !fields.isBlank()) {
            dto = applyFieldProjection(dto, fields);
        }
        return dto;
    }

    // ── SEARCH (list) ─────────────────────────────────────────

    @Transactional(readOnly = true)
    public Page<ProductDto> listProducts(
            String status,
            String customerId,
            String productOfferingId,
            String billingAccountId,
            String productOrderId,
            String name,
            Boolean isBundle,
            OffsetDateTime startDateFrom,
            OffsetDateTime startDateTo,
            String fields,
            int offset,
            int limit,
            String sort) {

        Pageable pageable = buildPageable(offset, limit, sort);
        Specification<Product> spec = ProductSpecification.buildFilter(
                status, customerId, productOfferingId, billingAccountId,
                productOrderId, name, isBundle, startDateFrom, startDateTo);

        Page<Product> page = productRepository.findAll(spec, pageable);

        List<ProductDto> dtos = page.getContent().stream()
                .map(mapper::toDto)
                .map(dto -> fields != null ? applyFieldProjection(dto, fields) : dto)
                .collect(Collectors.toList());

        return new PageImpl<>(dtos, pageable, page.getTotalElements());
    }

    // ── PATCH ────────────────────────────────────────────────

    public ProductDto patchProduct(String id, ProductUpdateDto dto) {
        Product product = productRepository.findByIdWithCollections(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        ProductStatusType oldStatus = product.getStatus();
        boolean statusChanged = false;

        // Validate status transition if status is changing
        if (dto.getStatus() != null) {
            ProductStatusType newStatus = parseStatus(dto.getStatus());
            if (newStatus != oldStatus) {
                ProductStatusMachine.validate(oldStatus, newStatus);
                statusChanged = true;
                appendStatusHistory(product, oldStatus, newStatus, "Status updated via PATCH", null);
            }
        }

        mapper.patchEntity(dto, product);

        // Replace collections if provided in PATCH body
        if (!CollectionUtils.isEmpty(dto.getProductCharacteristic())) {
            product.getProductCharacteristic().clear();
            dto.getProductCharacteristic().forEach(c -> {
                ProductCharacteristic e = mapper.toCharacteristicEntity(c);
                e.setProduct(product);
                product.getProductCharacteristic().add(e);
            });
        }
        if (!CollectionUtils.isEmpty(dto.getRelatedParty())) {
            product.getRelatedParty().clear();
            dto.getRelatedParty().forEach(r -> {
                RelatedParty e = mapper.toRelatedPartyEntity(r);
                e.setProduct(product);
                product.getRelatedParty().add(e);
            });
        }
        if (!CollectionUtils.isEmpty(dto.getNote())) {
            product.getNote().clear();
            dto.getNote().forEach(n -> {
                ProductNote e = mapper.toNoteEntity(n);
                e.setProduct(product);
                product.getNote().add(e);
            });
        }

        Product saved = productRepository.save(product);
        ProductDto result = mapper.toDto(saved);

        if (statusChanged) {
            eventPublisher.publishStateChange(result);
        } else {
            eventPublisher.publishAttributeChange(result);
        }

        log.info("Product patched: id={} statusChanged={}", id, statusChanged);
        return result;
    }

    // ── DELETE ────────────────────────────────────────────────

    public void deleteProduct(String id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        ProductDto dto = mapper.toDto(product);
        productRepository.delete(product);
        eventPublisher.publishDelete(dto);
        log.info("Product deleted: id={}", id);
    }

    // ── Private helpers ───────────────────────────────────────

    private void populateCollections(Product product, ProductCreateDto dto) {
        if (!CollectionUtils.isEmpty(dto.getProductCharacteristic())) {
            dto.getProductCharacteristic().forEach(c -> {
                ProductCharacteristic e = mapper.toCharacteristicEntity(c);
                e.setProduct(product);
                product.getProductCharacteristic().add(e);
            });
        }
        if (!CollectionUtils.isEmpty(dto.getProductRelationship())) {
            dto.getProductRelationship().forEach(r -> {
                ProductRelationship e = mapper.toRelationshipEntity(r);
                e.setProduct(product);
                product.getProductRelationship().add(e);
            });
        }
        if (!CollectionUtils.isEmpty(dto.getProductPrice())) {
            dto.getProductPrice().forEach(p -> {
                ProductPrice e = mapper.toPriceEntity(p);
                e.setProduct(product);
                product.getProductPrice().add(e);
            });
        }
        if (!CollectionUtils.isEmpty(dto.getProductTerm())) {
            dto.getProductTerm().forEach(t -> {
                ProductTerm e = mapper.toTermEntity(t);
                e.setProduct(product);
                product.getProductTerm().add(e);
            });
        }
        if (!CollectionUtils.isEmpty(dto.getRelatedParty())) {
            dto.getRelatedParty().forEach(r -> {
                RelatedParty e = mapper.toRelatedPartyEntity(r);
                e.setProduct(product);
                product.getRelatedParty().add(e);
            });
        }
        if (!CollectionUtils.isEmpty(dto.getPlace())) {
            dto.getPlace().forEach(p -> {
                ProductPlace e = mapper.toPlaceEntity(p);
                e.setProduct(product);
                product.getPlace().add(e);
            });
        }
        if (!CollectionUtils.isEmpty(dto.getProductOrderItem())) {
            dto.getProductOrderItem().forEach(o -> {
                ProductOrderRef e = mapper.toOrderRefEntity(o);
                e.setProduct(product);
                product.getProductOrderItem().add(e);
            });
        }
        if (!CollectionUtils.isEmpty(dto.getAgreement())) {
            dto.getAgreement().forEach(a -> {
                AgreementRef e = mapper.toAgreementRefEntity(a);
                e.setProduct(product);
                product.getAgreement().add(e);
            });
        }
        if (!CollectionUtils.isEmpty(dto.getNote())) {
            dto.getNote().forEach(n -> {
                ProductNote e = mapper.toNoteEntity(n);
                e.setProduct(product);
                product.getNote().add(e);
            });
        }
        if (!CollectionUtils.isEmpty(dto.getAttachment())) {
            dto.getAttachment().forEach(a -> {
                ProductAttachment e = mapper.toAttachmentEntity(a);
                e.setProduct(product);
                product.getAttachment().add(e);
            });
        }
        if (!CollectionUtils.isEmpty(dto.getRealizingResource())) {
            dto.getRealizingResource().forEach(r -> {
                RealizingResource e = mapper.toResourceEntity(r);
                e.setProduct(product);
                product.getRealizingResource().add(e);
            });
        }
        if (!CollectionUtils.isEmpty(dto.getRealizingService())) {
            dto.getRealizingService().forEach(s -> {
                RealizingService e = mapper.toServiceEntity(s);
                e.setProduct(product);
                product.getRealizingService().add(e);
            });
        }
    }

    private void appendStatusHistory(Product product,
                                     ProductStatusType from,
                                     ProductStatusType to,
                                     String reason,
                                     String orderRefId) {
        ProductStatusHistory h = ProductStatusHistory.builder()
                .product(product)
                .fromStatus(from != null ? from.name() : null)
                .toStatus(to.name())
                .reason(reason)
                .changedAt(OffsetDateTime.now())
                .orderRefId(orderRefId)
                .build();
        product.getStatusHistory().add(h);
    }

    private ProductStatusType parseStatus(String status) {
        try {
            return ProductStatusType.valueOf(status);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "Invalid status value '" + status + "'. Valid values: " +
                    Arrays.toString(ProductStatusType.values()));
        }
    }

    private Pageable buildPageable(int offset, int limit, String sort) {
        Sort pageSort = Sort.unsorted();
        if (sort != null && !sort.isBlank()) {
            // sort=name,-startDate  (prefix '-' = DESC)
            List<Sort.Order> orders = Arrays.stream(sort.split(","))
                    .map(String::trim)
                    .filter(s -> !s.isBlank())
                    .map(s -> s.startsWith("-")
                            ? Sort.Order.desc(s.substring(1))
                            : Sort.Order.asc(s))
                    .collect(Collectors.toList());
            pageSort = Sort.by(orders);
        }
        int page = offset / Math.max(limit, 1);
        return PageRequest.of(page, limit, pageSort);
    }

    /**
     * TMF 'fields' parameter: return only specified top-level properties.
     * Nulls out fields not in the requested set; Jackson @JsonInclude(NON_NULL)
     * then omits them from the JSON response.
     */
    private ProductDto applyFieldProjection(ProductDto dto, String fields) {
        Set<String> requested = Arrays.stream(fields.split(","))
                .map(String::trim)
                .collect(Collectors.toSet());

        if (!requested.contains("name"))                    dto.setName(null);
        if (!requested.contains("description"))             dto.setDescription(null);
        if (!requested.contains("status"))                  dto.setStatus(null);
        if (!requested.contains("isBundle"))                dto.setIsBundle(null);
        if (!requested.contains("isCustomerVisible"))       dto.setIsCustomerVisible(null);
        if (!requested.contains("productSerialNumber"))     dto.setProductSerialNumber(null);
        if (!requested.contains("orderDate"))               dto.setOrderDate(null);
        if (!requested.contains("startDate"))               dto.setStartDate(null);
        if (!requested.contains("terminationDate"))         dto.setTerminationDate(null);
        if (!requested.contains("billingAccount"))          dto.setBillingAccount(null);
        if (!requested.contains("productOffering"))         dto.setProductOffering(null);
        if (!requested.contains("productSpecification"))    dto.setProductSpecification(null);
        if (!requested.contains("productCharacteristic"))   dto.setProductCharacteristic(null);
        if (!requested.contains("productRelationship"))     dto.setProductRelationship(null);
        if (!requested.contains("productPrice"))            dto.setProductPrice(null);
        if (!requested.contains("productTerm"))             dto.setProductTerm(null);
        if (!requested.contains("relatedParty"))            dto.setRelatedParty(null);
        if (!requested.contains("place"))                   dto.setPlace(null);
        if (!requested.contains("productOrderItem"))        dto.setProductOrderItem(null);
        if (!requested.contains("agreement"))               dto.setAgreement(null);
        if (!requested.contains("note"))                    dto.setNote(null);
        if (!requested.contains("attachment"))              dto.setAttachment(null);
        if (!requested.contains("realizingResource"))       dto.setRealizingResource(null);
        if (!requested.contains("realizingService"))        dto.setRealizingService(null);
        return dto;
    }
}
