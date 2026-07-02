package com.jio.productinventory.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaAuditing
@EnableJpaRepositories(basePackages = "com.jio.productinventory.repository")
public class JpaConfig {
    // Spring Security integration point: override AuditorAware to pull from JWT principal.
    // Currently returns empty (no auth context in MVP).
}
