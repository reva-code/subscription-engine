package com.subscriptionengine.productusage.application.service;

import com.subscriptionengine.productusage.domain.exception.ActiveSubscriptionValidationException;
import com.subscriptionengine.productusage.domain.exception.PlanLimitExceededException;
import com.subscriptionengine.productusage.domain.exception.UsageValidationException;
import com.subscriptionengine.productusage.domain.model.UsageConsumption;
import com.subscriptionengine.productusage.domain.port.in.UsageValidationService;
import com.subscriptionengine.productusage.domain.port.out.ProductInventoryPort;
import com.subscriptionengine.productusage.domain.port.out.ProductCatalogPort;
import com.subscriptionengine.productusage.domain.port.out.UsageConsumptionRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DefaultUsageValidationService implements UsageValidationService {

    private final ProductInventoryPort productInventoryPort;
    private final ProductCatalogPort productCatalogPort;
    private final UsageConsumptionRepositoryPort usageConsumptionRepositoryPort;

    public DefaultUsageValidationService(ProductInventoryPort productInventoryPort,
                                         ProductCatalogPort productCatalogPort,
                                         UsageConsumptionRepositoryPort usageConsumptionRepositoryPort) {
        this.productInventoryPort = productInventoryPort;
        this.productCatalogPort = productCatalogPort;
        this.usageConsumptionRepositoryPort = usageConsumptionRepositoryPort;
    }

    @Override
    public void validateUsage(UsageConsumption usageConsumption) {
        if (usageConsumption.getProduct() == null || usageConsumption.getProduct().isEmpty()) {
            throw new UsageValidationException("Usage must be tied to a ProductRef.");
        }

        String productId = usageConsumption.getProduct().get(0).getId();

        // 1. Active Subscription Validation via TMF637 Product Inventory
        Optional<String> offeringIdOpt = productInventoryPort.getActiveProductOfferingId(productId);
        if (offeringIdOpt.isEmpty()) {
            throw new ActiveSubscriptionValidationException("Subscription is not ACTIVE for product: " + productId);
        }

        // 2. Plan Limit Validation via TMF620 Product Catalog
        String offeringId = offeringIdOpt.get();
        Optional<Double> limitOpt = productCatalogPort.getUsageLimit(offeringId);
        
        if (limitOpt.isPresent()) {
            double limit = limitOpt.get();
            
            // Calculate previous usage (Mocking 1 unit of consumption per usage event for testing)
            List<UsageConsumption> pastUsages = usageConsumptionRepositoryPort.findTotalUsageByProductId(productId);
            double totalPastUsage = pastUsages.size() * 1.0;

            // Calculate new usage from incoming event (Mocking 1 unit of consumption)
            double newUsage = 1.0;

            if ((totalPastUsage + newUsage) > limit) {
                throw new PlanLimitExceededException(
                        String.format("Usage exceeds plan limits for product: %s. Limit: %s, Current Total: %s",
                                productId, limit, (totalPastUsage + newUsage)));
            }
        }
    }
}
