package com.jio.productinventory.repository;

import com.jio.productinventory.entity.Product;
import com.jio.productinventory.entity.ProductStatusType;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * JPA Specifications for dynamic TMF637 filtering.
 * Composes predicates from query parameters so the controller
 * can build arbitrary filter combinations without N repository methods.
 */
public final class ProductSpecification {

    private ProductSpecification() {}

    public static Specification<Product> buildFilter(
            String status,
            String customerId,
            String productOfferingId,
            String billingAccountId,
            String productOrderId,
            String name,
            Boolean isBundle,
            OffsetDateTime startDateFrom,
            OffsetDateTime startDateTo) {

        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.hasText(status)) {
                try {
                    predicates.add(cb.equal(root.get("status"),
                            ProductStatusType.valueOf(status)));
                } catch (IllegalArgumentException ignored) {
                    // invalid status value → no results (handled by validator)
                }
            }

            if (StringUtils.hasText(customerId)) {
                Subquery<Long> sub = query.subquery(Long.class);
                var relatedPartyRoot = sub.from(
                        com.jio.productinventory.entity.RelatedParty.class);
                sub.select(cb.literal(1L))
                   .where(
                        cb.equal(relatedPartyRoot.get("product"), root),
                        cb.equal(relatedPartyRoot.get("partyId"), customerId),
                        cb.equal(relatedPartyRoot.get("role"), "Customer")
                   );
                predicates.add(cb.exists(sub));
            }

            if (StringUtils.hasText(productOfferingId)) {
                predicates.add(cb.equal(root.get("productOfferingId"), productOfferingId));
            }

            if (StringUtils.hasText(billingAccountId)) {
                predicates.add(cb.equal(root.get("billingAccountId"), billingAccountId));
            }

            if (StringUtils.hasText(productOrderId)) {
                Subquery<Long> sub = query.subquery(Long.class);
                var orderRoot = sub.from(
                        com.jio.productinventory.entity.ProductOrderRef.class);
                sub.select(cb.literal(1L))
                   .where(
                        cb.equal(orderRoot.get("product"), root),
                        cb.equal(orderRoot.get("productOrderId"), productOrderId)
                   );
                predicates.add(cb.exists(sub));
            }

            if (StringUtils.hasText(name)) {
                predicates.add(cb.like(cb.lower(root.get("name")),
                        "%" + name.toLowerCase() + "%"));
            }

            if (isBundle != null) {
                predicates.add(cb.equal(root.get("isBundle"), isBundle));
            }

            if (startDateFrom != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("startDate"), startDateFrom));
            }

            if (startDateTo != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("startDate"), startDateTo));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
