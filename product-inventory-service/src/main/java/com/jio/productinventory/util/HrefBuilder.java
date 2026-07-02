package com.jio.productinventory.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class HrefBuilder {

    @Value("${app.base-path:/tmf-api/productInventoryManagement/v4}")
    private String basePath;

    @Value("${server.port:8084}")
    private String port;

    public String buildProductHref(String id) {
        return "http://localhost:" + port + basePath + "/product/" + id;
    }
}
