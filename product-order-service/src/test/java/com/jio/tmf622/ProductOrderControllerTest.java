package com.jio.tmf622;

import com.jio.tmf622.controller.ProductOrderController;
import com.jio.tmf622.service.ProductOrderService;
import com.jio.tmf622.entity.ProductOrderEntity;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductOrderController.class)
public class ProductOrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductOrderService productOrderService;

    @Test
    void createProductOrder_shouldReturn201() throws Exception {

        String request = """
        {
          "productOrderItem": [
            {
              "id": "1",
              "action": "add"
            }
          ]
        }
        """;

        ProductOrderEntity entity = new ProductOrderEntity();
        entity.setId("123");
        entity.setOrderDate(OffsetDateTime.now());
        entity.setState(com.jio.tmf622.model.ProductOrderStateType.ACKNOWLEDGED);

        when(productOrderService.createProductOrder(any()))
                .thenReturn(entity);

        mockMvc.perform(post("/productOrder")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
                .andExpect(status().isCreated());
    }

    @Test
    void retrieveProductOrder_shouldReturn200() throws Exception {

        ProductOrderEntity entity = new ProductOrderEntity();
        entity.setId("123");
        entity.setOrderDate(OffsetDateTime.now());
        entity.setState(com.jio.tmf622.model.ProductOrderStateType.ACKNOWLEDGED);

        when(productOrderService.getProductOrderById("123"))
                .thenReturn(java.util.Optional.of(entity));

        mockMvc.perform(get("/productOrder/123"))
                .andExpect(status().isOk());
    }
    
    @Test
void retrieveProductOrder_shouldReturn404() throws Exception {

    when(productOrderService.getProductOrderById("999"))
            .thenReturn(Optional.empty());

    mockMvc.perform(get("/productOrder/999"))
            .andExpect(status().isNotFound());
}
}