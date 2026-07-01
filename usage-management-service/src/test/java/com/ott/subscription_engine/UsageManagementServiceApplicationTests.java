package com.ott.subscription_engine;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ott.subscription_engine.tmf635_usage.model.UsageCharacteristic;
import com.ott.subscription_engine.tmf635_usage.model.UsageCreate;
import com.ott.subscription_engine.tmf635_usage.model.UsageStatusType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = UsageManagementServiceApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class UsageManagementServiceApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void contextLoads() {
    }

    @Test
    void testCreateAndGetUsage() throws Exception {
        UsageCreate usageCreate = new UsageCreate();
        usageCreate.setDescription("Streaming Video consumption");
        usageCreate.setUsageType("video");
        usageCreate.setUsageDate(OffsetDateTime.parse("2026-06-15T18:00:00Z"));
        usageCreate.setStatus(UsageStatusType.RECEIVED);

        UsageCharacteristic quantityChar = new UsageCharacteristic();
        quantityChar.setName("quantity");
        quantityChar.setValue("1024");
        UsageCharacteristic unitChar = new UsageCharacteristic();
        unitChar.setName("unit");
        unitChar.setValue("MB");

        usageCreate.setUsageCharacteristic(List.of(quantityChar, unitChar));

        // Create usage log
        mockMvc.perform(post("/tmf-api/usageManagement/v4/usage")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usageCreate)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.description", is("Streaming Video consumption")))
                .andExpect(jsonPath("$.usageType", is("video")))
                .andExpect(jsonPath("$.status", is("received")));

        // Get usage logs list
        mockMvc.perform(get("/tmf-api/usageManagement/v4/usage"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
    }
}
