package com.jio.productinventory.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jio.productinventory.dto.*;
import com.jio.productinventory.entity.*;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * MapStruct mapper: Entity ↔ DTO.
 * Uses SPRING component model so it can be @Autowired.
 * @BeanMapping(nullValuePropertyMappingStrategy = IGNORE) implements PATCH semantics.
 */
@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class ProductMapper {

    @Autowired
    protected ObjectMapper objectMapper;

    // ── Entity → DTO ─────────────────────────────────────────

    @Mapping(target = "billingAccount", expression = "java(toBillingAccountRef(p))")
    @Mapping(target = "productOffering", expression = "java(toProductOfferingRef(p))")
    @Mapping(target = "productSpecification", expression = "java(toProductSpecRef(p))")
    @Mapping(target = "productCharacteristic", source = "productCharacteristic")
    @Mapping(target = "productRelationship", source = "productRelationship")
    @Mapping(target = "productPrice", source = "productPrice")
    @Mapping(target = "productTerm", source = "productTerm")
    @Mapping(target = "relatedParty", source = "relatedParty")
    @Mapping(target = "place", source = "place")
    @Mapping(target = "productOrderItem", source = "productOrderItem")
    @Mapping(target = "agreement", source = "agreement")
    @Mapping(target = "note", source = "note")
    @Mapping(target = "attachment", source = "attachment")
    @Mapping(target = "realizingResource", source = "realizingResource")
    @Mapping(target = "realizingService", source = "realizingService")
    @Mapping(target = "status", expression = "java(p.getStatus() != null ? p.getStatus().name() : null)")
    public abstract ProductDto toDto(Product p);

    public abstract List<ProductDto> toDtoList(List<Product> products);

    // ── DTO → Entity (CREATE) ─────────────────────────────────
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "href", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "statusHistory", ignore = true)
    @Mapping(target = "billingAccountId",   expression = "java(dto.getBillingAccount() != null ? dto.getBillingAccount().getId() : null)")
    @Mapping(target = "billingAccountHref", expression = "java(dto.getBillingAccount() != null ? dto.getBillingAccount().getHref() : null)")
    @Mapping(target = "billingAccountName", expression = "java(dto.getBillingAccount() != null ? dto.getBillingAccount().getName() : null)")
    @Mapping(target = "productOfferingId",   expression = "java(dto.getProductOffering() != null ? dto.getProductOffering().getId() : null)")
    @Mapping(target = "productOfferingHref", expression = "java(dto.getProductOffering() != null ? dto.getProductOffering().getHref() : null)")
    @Mapping(target = "productOfferingName", expression = "java(dto.getProductOffering() != null ? dto.getProductOffering().getName() : null)")
    @Mapping(target = "productSpecId",      expression = "java(dto.getProductSpecification() != null ? dto.getProductSpecification().getId() : null)")
    @Mapping(target = "productSpecHref",    expression = "java(dto.getProductSpecification() != null ? dto.getProductSpecification().getHref() : null)")
    @Mapping(target = "productSpecName",    expression = "java(dto.getProductSpecification() != null ? dto.getProductSpecification().getName() : null)")
    @Mapping(target = "productSpecVersion", expression = "java(dto.getProductSpecification() != null ? dto.getProductSpecification().getVersion() : null)")
    @Mapping(target = "status", expression = "java(com.jio.productinventory.entity.ProductStatusType.valueOf(dto.getStatus()))")
    @Mapping(target = "productCharacteristic", ignore = true)
    @Mapping(target = "productRelationship", ignore = true)
    @Mapping(target = "productPrice", ignore = true)
    @Mapping(target = "productTerm", ignore = true)
    @Mapping(target = "relatedParty", ignore = true)
    @Mapping(target = "place", ignore = true)
    @Mapping(target = "productOrderItem", ignore = true)
    @Mapping(target = "agreement", ignore = true)
    @Mapping(target = "note", ignore = true)
    @Mapping(target = "attachment", ignore = true)
    @Mapping(target = "realizingResource", ignore = true)
    @Mapping(target = "realizingService", ignore = true)
    public abstract Product toEntity(ProductCreateDto dto);

    // ── PATCH: merge non-null DTO fields into existing entity ──
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "href", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "statusHistory", ignore = true)
    @Mapping(target = "productCharacteristic", ignore = true)
    @Mapping(target = "productRelationship", ignore = true)
    @Mapping(target = "productPrice", ignore = true)
    @Mapping(target = "productTerm", ignore = true)
    @Mapping(target = "relatedParty", ignore = true)
    @Mapping(target = "place", ignore = true)
    @Mapping(target = "productOrderItem", ignore = true)
    @Mapping(target = "agreement", ignore = true)
    @Mapping(target = "note", ignore = true)
    @Mapping(target = "attachment", ignore = true)
    @Mapping(target = "realizingResource", ignore = true)
    @Mapping(target = "realizingService", ignore = true)
    @Mapping(target = "billingAccountId",   expression = "java(dto.getBillingAccount() != null ? dto.getBillingAccount().getId() : entity.getBillingAccountId())")
    @Mapping(target = "billingAccountHref", expression = "java(dto.getBillingAccount() != null ? dto.getBillingAccount().getHref() : entity.getBillingAccountHref())")
    @Mapping(target = "billingAccountName", expression = "java(dto.getBillingAccount() != null ? dto.getBillingAccount().getName() : entity.getBillingAccountName())")
    @Mapping(target = "productOfferingId",   expression = "java(dto.getProductOffering() != null ? dto.getProductOffering().getId() : entity.getProductOfferingId())")
    @Mapping(target = "productOfferingHref", expression = "java(dto.getProductOffering() != null ? dto.getProductOffering().getHref() : entity.getProductOfferingHref())")
    @Mapping(target = "productOfferingName", expression = "java(dto.getProductOffering() != null ? dto.getProductOffering().getName() : entity.getProductOfferingName())")
    @Mapping(target = "productSpecId",      expression = "java(dto.getProductSpecification() != null ? dto.getProductSpecification().getId() : entity.getProductSpecId())")
    @Mapping(target = "productSpecHref",    expression = "java(dto.getProductSpecification() != null ? dto.getProductSpecification().getHref() : entity.getProductSpecHref())")
    @Mapping(target = "productSpecName",    expression = "java(dto.getProductSpecification() != null ? dto.getProductSpecification().getName() : entity.getProductSpecName())")
    @Mapping(target = "productSpecVersion", expression = "java(dto.getProductSpecification() != null ? dto.getProductSpecification().getVersion() : entity.getProductSpecVersion())")
    @Mapping(target = "status", expression = "java(dto.getStatus() != null ? com.jio.productinventory.entity.ProductStatusType.valueOf(dto.getStatus()) : entity.getStatus())")
    public abstract void patchEntity(ProductUpdateDto dto, @MappingTarget Product entity);

    // ── Collection mappers ────────────────────────────────────

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "value", expression = "java(serializeCharacteristicValue(dto.getValue()))")
    public abstract ProductCharacteristic toCharacteristicEntity(ProductCharacteristicDto dto);

    @Mapping(target = "value", expression = "java(deserializeCharacteristicValue(entity.getValue(), entity.getValueType()))")
    public abstract ProductCharacteristicDto toCharacteristicDto(ProductCharacteristic entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "relatedProductId", source = "product.id")
    @Mapping(target = "relatedProductHref", source = "product.href")
    @Mapping(target = "relatedProductName", source = "product.name")
    public abstract ProductRelationship toRelationshipEntity(ProductRelationshipDto dto);

    @Mapping(target = "product", expression = "java(toProductRef(entity))")
    public abstract ProductRelationshipDto toRelationshipDto(ProductRelationship entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "partyId", source = "id")
    public abstract RelatedParty toRelatedPartyEntity(RelatedPartyDto dto);

    @Mapping(target = "id", source = "partyId")
    public abstract RelatedPartyDto toRelatedPartyDto(RelatedParty entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "dutyFreeAmount", expression = "java(dto.getPrice() != null && dto.getPrice().getDutyFreeAmount() != null ? dto.getPrice().getDutyFreeAmount().getValue() : null)")
    @Mapping(target = "taxIncludedAmount", expression = "java(dto.getPrice() != null && dto.getPrice().getTaxIncludedAmount() != null ? dto.getPrice().getTaxIncludedAmount().getValue() : null)")
    @Mapping(target = "taxRate", expression = "java(dto.getPrice() != null ? dto.getPrice().getTaxRate() : null)")
    @Mapping(target = "percentage", expression = "java(dto.getPrice() != null ? dto.getPrice().getPercentage() : null)")
    @Mapping(target = "currencyUnit", expression = "java(dto.getPrice() != null && dto.getPrice().getDutyFreeAmount() != null ? dto.getPrice().getDutyFreeAmount().getUnit() : null)")
    @Mapping(target = "popId", expression = "java(dto.getProductOfferingPrice() != null ? dto.getProductOfferingPrice().getId() : null)")
    @Mapping(target = "popHref", expression = "java(dto.getProductOfferingPrice() != null ? dto.getProductOfferingPrice().getHref() : null)")
    @Mapping(target = "popName", expression = "java(dto.getProductOfferingPrice() != null ? dto.getProductOfferingPrice().getName() : null)")
    @Mapping(target = "billingAccountId", expression = "java(dto.getBillingAccount() != null ? dto.getBillingAccount().getId() : null)")
    @Mapping(target = "billingAccountHref", expression = "java(dto.getBillingAccount() != null ? dto.getBillingAccount().getHref() : null)")
    @Mapping(target = "productPriceAlteration", ignore = true)
    public abstract ProductPrice toPriceEntity(ProductPriceDto dto);

    public abstract ProductPriceDto toPriceDto(ProductPrice entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "durationAmount", expression = "java(dto.getDuration() != null ? dto.getDuration().getAmount() : null)")
    @Mapping(target = "durationUnits", expression = "java(dto.getDuration() != null ? dto.getDuration().getUnits() : null)")
    @Mapping(target = "validForStart", expression = "java(dto.getValidFor() != null ? dto.getValidFor().getStartDateTime() : null)")
    @Mapping(target = "validForEnd", expression = "java(dto.getValidFor() != null ? dto.getValidFor().getEndDateTime() : null)")
    public abstract ProductTerm toTermEntity(ProductTermDto dto);

    @Mapping(target = "duration", expression = "java(toQuantityDto(entity))")
    @Mapping(target = "validFor", expression = "java(toTimePeriodDto(entity))")
    public abstract ProductTermDto toTermDto(ProductTerm entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "placeId", source = "id")
    public abstract ProductPlace toPlaceEntity(ProductDto.ProductPlaceDto dto);

    @Mapping(target = "id", source = "placeId")
    public abstract ProductDto.ProductPlaceDto toPlaceDto(ProductPlace entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", ignore = true)
    public abstract ProductOrderRef toOrderRefEntity(ProductDto.ProductOrderItemDto dto);

    public abstract ProductDto.ProductOrderItemDto toOrderRefDto(ProductOrderRef entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "agreementId", source = "id")
    public abstract AgreementRef toAgreementRefEntity(ProductDto.AgreementRefDto dto);

    @Mapping(target = "id", source = "agreementId")
    public abstract ProductDto.AgreementRefDto toAgreementRefDto(AgreementRef entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", ignore = true)
    public abstract ProductNote toNoteEntity(ProductDto.ProductNoteDto dto);

    public abstract ProductDto.ProductNoteDto toNoteDto(ProductNote entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "attachmentId", source = "id")
    public abstract ProductAttachment toAttachmentEntity(ProductDto.ProductAttachmentDto dto);

    @Mapping(target = "id", source = "attachmentId")
    public abstract ProductDto.ProductAttachmentDto toAttachmentDto(ProductAttachment entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "resourceId", source = "id")
    public abstract RealizingResource toResourceEntity(ProductDto.RealizingResourceDto dto);

    @Mapping(target = "id", source = "resourceId")
    public abstract ProductDto.RealizingResourceDto toResourceDto(RealizingResource entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "serviceId", source = "id")
    public abstract RealizingService toServiceEntity(ProductDto.RealizingServiceDto dto);

    @Mapping(target = "id", source = "serviceId")
    public abstract ProductDto.RealizingServiceDto toServiceDto(RealizingService entity);

    // ── Helper methods ────────────────────────────────────────

    protected BillingAccountRefDto toBillingAccountRef(Product p) {
        if (p.getBillingAccountId() == null) return null;
        return BillingAccountRefDto.builder()
                .id(p.getBillingAccountId())
                .href(p.getBillingAccountHref())
                .name(p.getBillingAccountName())
                .build();
    }

    protected ProductDto.ProductOfferingRefDto toProductOfferingRef(Product p) {
        if (p.getProductOfferingId() == null) return null;
        return ProductDto.ProductOfferingRefDto.builder()
                .id(p.getProductOfferingId())
                .href(p.getProductOfferingHref())
                .name(p.getProductOfferingName())
                .build();
    }

    protected ProductDto.ProductSpecificationRefDto toProductSpecRef(Product p) {
        if (p.getProductSpecId() == null) return null;
        return ProductDto.ProductSpecificationRefDto.builder()
                .id(p.getProductSpecId())
                .href(p.getProductSpecHref())
                .name(p.getProductSpecName())
                .version(p.getProductSpecVersion())
                .build();
    }

    protected ProductRelationshipDto.ProductRefDto toProductRef(ProductRelationship rel) {
        return ProductRelationshipDto.ProductRefDto.builder()
                .id(rel.getRelatedProductId())
                .href(rel.getRelatedProductHref())
                .name(rel.getRelatedProductName())
                .build();
    }

    protected ProductTermDto.QuantityDto toQuantityDto(ProductTerm term) {
        if (term.getDurationAmount() == null && term.getDurationUnits() == null) return null;
        return ProductTermDto.QuantityDto.builder()
                .amount(term.getDurationAmount())
                .units(term.getDurationUnits())
                .build();
    }

    protected ProductTermDto.TimePeriodDto toTimePeriodDto(ProductTerm term) {
        if (term.getValidForStart() == null && term.getValidForEnd() == null) return null;
        return ProductTermDto.TimePeriodDto.builder()
                .startDateTime(term.getValidForStart())
                .endDateTime(term.getValidForEnd())
                .build();
    }

    protected String serializeCharacteristicValue(Object value) {
        if (value == null) return null;
        if (value instanceof String s) return s;
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            return value.toString();
        }
    }

    protected Object deserializeCharacteristicValue(String value, String valueType) {
        if (value == null) return null;
        if ("object".equalsIgnoreCase(valueType)) {
            try {
                return objectMapper.readValue(value, Object.class);
            } catch (JsonProcessingException e) {
                return value;
            }
        }
        return value;
    }
}
