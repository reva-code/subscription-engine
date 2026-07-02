package com.subscriptionengine.productusage.domain.port.out;

import java.util.Optional;

public interface ProductInventoryPort {
    /**
     * Checks if a product exists and is in the ACTIVE state.
     * If active, returns the associated ProductOffering ID.
     *
     * @param productId the unique identifier of the product
     * @return Optional containing the ProductOfferingId if active, empty otherwise
     */
    Optional<String> getActiveProductOfferingId(String productId);
}
