package com.subscriptionengine.productusage.infrastructure.adapter;

import com.fasterxml.jackson.databind.JsonNode;
import com.subscriptionengine.productusage.domain.port.out.ProductInventoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Component
public class ProductInventoryClientAdapter implements ProductInventoryPort {

    private static final Logger log = LoggerFactory.getLogger(ProductInventoryClientAdapter.class);

    private final WebClient webClient;

    public ProductInventoryClientAdapter(WebClient.Builder webClientBuilder,
                                         @Value("${jio.integration.tmf637.base-url}") String baseUrl) {
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

    @Override
    public java.util.Optional<String> getActiveProductOfferingId(String productId) {
        try {
            log.info("Calling TMF637 to verify status for product: {}", productId);
            JsonNode response = webClient.get()
                    .uri("/product/{id}", productId)
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();

            if (response != null && response.has("status")) {
                String status = response.get("status").asText();
                log.info("TMF637 returned status {} for product {}", status, productId);
                if ("ACTIVE".equalsIgnoreCase(status) && response.has("productOffering")) {
                    return java.util.Optional.ofNullable(response.get("productOffering").get("id").asText());
                }
            }
            return java.util.Optional.empty();
        } catch (WebClientResponseException.NotFound ex) {
            log.warn("Product {} not found in TMF637", productId);
            return java.util.Optional.empty();
        } catch (Exception ex) {
            log.error("Error communicating with TMF637 for product {}: {}", productId, ex.getMessage());
            return java.util.Optional.empty();
        }
    }
}
