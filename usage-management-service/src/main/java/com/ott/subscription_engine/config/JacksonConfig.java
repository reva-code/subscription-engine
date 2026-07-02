package com.ott.subscription_engine.config;

/**
 * Gap 5.1 fix: This local JacksonConfig is intentionally left empty.
 *
 * The canonical Jackson configuration now lives in tmf-common:
 *   com.ott.subscription_engine.common.SharedJacksonConfig
 *
 * That class is auto-loaded via Spring component scan since tmf-common
 * is a dependency of this service and its base package is scanned.
 *
 * DO NOT re-add ObjectMapper @Bean here — it will conflict with SharedJacksonConfig.
 */
public class JacksonConfig {
    // Intentionally empty — see SharedJacksonConfig in tmf-common
}
