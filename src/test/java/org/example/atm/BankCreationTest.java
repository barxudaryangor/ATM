package org.example.atm;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.atm.controllers.BankController;
import org.example.atm.dtos.BankDTO;
import org.example.atm.services.BankServiceImpl;
import org.example.atm.short_dtos.BankAccountShortDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistrar;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@Testcontainers
public class BankCreationTest extends AbstractPostgresContainerTest{

    @Autowired
    WebApplicationContext context;

    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void setUpMockMvc() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    void testCreateBank() throws Exception {
        BankDTO request = new BankDTO(
                null,
                "AraratBank",
                "Erevan",
                List.of(new BankAccountShortDTO(
                        null, "GB220205", 1000.0
                ))
        );

        String requestBody = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/atm/banks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andDo(result -> {
                    System.out.println("==== RESPONSE BODY ====");
                    System.out.println(result.getResponse().getContentAsString());
                    System.out.println("=======================");
                })
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("AraratBank"))
                .andExpect(jsonPath("$.location").value("Erevan"))
                .andExpect(jsonPath("$.bankAccounts[0].id").isNumber())
                .andExpect(jsonPath("$.bankAccounts[0].account_num").value("GB220205"))
                .andExpect(jsonPath("$.bankAccounts[0].balance").value(1000.0));
    }
}
