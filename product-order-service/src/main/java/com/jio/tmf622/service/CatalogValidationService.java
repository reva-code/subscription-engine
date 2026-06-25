package com.jio.tmf622.service;

import org.springframework.stereotype.Service;

@Service
public class CatalogValidationService {

    public void validateOffering(String offeringId) {

        if (offeringId == null || offeringId.isBlank()) {
            throw new IllegalArgumentException(
                    "Product offering ID is required");
        }

        // TODO: Replace with actual TMF620 Catalog API call
        System.out.println(
                "Validated product offering: " + offeringId);
    }
}