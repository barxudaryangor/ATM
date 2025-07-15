package org.example.atm;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.atm.controllers.BankController;
import org.example.atm.dtos.BankDTO;
import org.example.atm.services.BankServiceImpl;
import org.example.atm.short_dtos.BankAccountShortDTO;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(BankController.class)

public class BankCreationTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    BankServiceImpl bankService;

    @Test
    void testCreateBank() throws Exception {
        BankDTO request = new BankDTO(
                null,
                "AraratBank",
                "Erevan",
                List.of(new BankAccountShortDTO(
                        1L, "GB220205", 1000.0
                ))
        );

        BankDTO response = new BankDTO(
                1L,
                "AraratBank",
                "Erevan",
                List.of(new BankAccountShortDTO(
                        1L, "GB220205", 1000.0
                ))
        );

        String requestBody = objectMapper.writeValueAsString(request);
        Mockito.when(bankService.createBank(any(BankDTO.class))).thenReturn(response);

        mockMvc.perform(post("/atm/banks")
                    .content(requestBody)
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("AraratBank"))
                .andExpect(jsonPath("$.location").value("Erevan"))
                .andExpect(jsonPath("$.bankAccounts[0].id").value(1))
                .andExpect(jsonPath("$.bankAccounts[0].account_num").value("GB220205"))
                .andExpect(jsonPath("$.bankAccounts[0].balance").value(1000.0));


    }
}
