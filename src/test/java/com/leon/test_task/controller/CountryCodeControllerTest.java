package com.leon.test_task.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class CountryCodeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void should_return_country_by_code() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/country?phone=0011112223"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Country not found")));
    }

    @Test
    void should_return_validation_error() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/country?phone=000"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Phone number is not valid")));
    }
}