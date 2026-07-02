package com.ott.subscription_engine.tmf635_usage.service;

import java.util.List;

/**
 * Public API of the TMF635 Usage module.
 */
public interface UsageServicePort {
    long countUsageByCustomer(String customerId);
    List<String> getUsageIdsByCustomer(String customerId);
}
