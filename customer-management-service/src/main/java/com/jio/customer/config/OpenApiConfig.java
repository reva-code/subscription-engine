package com.jio.customer.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customerManagementOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("TMF629 Customer Management API")
                        .version("4.0.0")
                        .description("Manages Customers and their CustomerAccounts. " +
                                     "Customers reference an Individual in TMF632 via engagedPartyId.")
                        .contact(new Contact()
                                .name("Jio Subscription Engine")
                                .email("saanvi3605@gmail.com")));
    }
}
