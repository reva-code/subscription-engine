package product_catalog_service.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {

        return new OpenAPI()
                .info(
                        new Info()
                                .title("TMF620 Product Catalog API")
                                .version("1.0")
                                .description(
                                        "Product Catalog Service for Atlas Telecom")
                                .contact(
                                        new Contact()
                                                .name("Atlas Telecom")
                                                .email("support@atlas.com")
                                )
                );
    }

}