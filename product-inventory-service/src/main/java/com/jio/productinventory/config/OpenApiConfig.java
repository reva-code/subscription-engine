package com.jio.productinventory.config;

import io.swagger.v3.oas.models.*;
import io.swagger.v3.oas.models.info.*;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.*;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI productInventoryOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("TMF637 Product Inventory Management API")
                        .version("4.0.0")
                        .description("Jio Subscription Engine — manages active customer products/subscriptions. " +
                                     "Compliant with TM Forum TMF637 v4.0.0.")
                        .contact(new Contact()
                                .name("Jio Platform Engineering")
                                .email("platform-engineering@jio.com"))
                        .license(new License()
                                .name("Apache 2.0")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8084")
                                .description("Local Development"),
                        new Server()
                                .url("https://api.jio.com")
                                .description("Production")));
    }
}
