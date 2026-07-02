package com.subscriptionengine.productusage.domain.port.out;

import java.util.Optional;

public interface ProductCatalogPort {
    /**
     * Retrieves the usage limit for a given ProductOffering ID.
     * Searches for a ProductCharacteristic named "DataLimit" or "UsageLimit".
     *
     * @param productOfferingId the unique identifier of the product offering
     * @return Optional containing the numeric limit if found, empty otherwise
     */
    Optional<Double> getUsageLimit(String productOfferingId);
}
