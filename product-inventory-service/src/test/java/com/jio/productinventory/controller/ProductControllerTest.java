package com.jio.productinventory.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jio.productinventory.dto.*;
import com.jio.productinventory.exception.*;
import com.jio.productinventory.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    private static final String BASE = "/tmf-api/productInventoryManagement/v4";

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @MockBean  ProductService productService;

    @Test
    void createProduct_returns201() throws Exception {
        ProductCreateDto request = ProductCreateDto.builder()
                .name("Jio Prime")
                .status("created")
                .build();

        ProductDto response = ProductDto.builder()
                .id("uuid-1")
                .name("Jio Prime")
                .status("created")
                .build();

        when(productService.createProduct(any())).thenReturn(response);

        mockMvc.perform(post(BASE + "/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("uuid-1"))
                .andExpect(jsonPath("$.status").value("created"))
                .andExpect(header().exists("Location"));
    }

    @Test
    void getProduct_notFound_returns404() throws Exception {
        when(productService.getProduct(eq("missing"), any()))
                .thenThrow(new ProductNotFoundException("missing"));

        mockMvc.perform(get(BASE + "/product/missing"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("404"));
    }

    @Test
    void listProducts_returnsPaginationHeaders() throws Exception {
        ProductDto dto = ProductDto.builder().id("id-1").status("active").build();
        Page<ProductDto> page = new PageImpl<>(List.of(dto), PageRequest.of(0, 20), 1);

        when(productService.listProducts(any(), any(), any(), any(), any(), any(),
                any(), any(), any(), any(), anyInt(), anyInt(), any()))
                .thenReturn(page);

        mockMvc.perform(get(BASE + "/product").param("limit", "20").param("offset", "0"))
                .andExpect(status().isOk())
                .andExpect(header().string("X-Total-Count", "1"))
                .andExpect(header().string("X-Result-Count", "1"))
                .andExpect(jsonPath("$[0].id").value("id-1"));
    }

    @Test
    void patchProduct_invalidTransition_returns422() throws Exception {
        ProductUpdateDto dto = ProductUpdateDto.builder().status("active").build();

        when(productService.patchProduct(eq("id-1"), any()))
                .thenThrow(new InvalidStatusTransitionException("Transition not permitted"));

        mockMvc.perform(patch(BASE + "/product/id-1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.code").value("422"));
    }

    @Test
    void deleteProduct_returns204() throws Exception {
        doNothing().when(productService).deleteProduct("id-1");

        mockMvc.perform(delete(BASE + "/product/id-1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void createProduct_missingStatus_returns400() throws Exception {
        ProductCreateDto request = ProductCreateDto.builder()
                .name("Jio Prime")
                // status intentionally omitted
                .build();

        mockMvc.perform(post(BASE + "/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
