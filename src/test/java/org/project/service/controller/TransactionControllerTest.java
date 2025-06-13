package org.project.service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.project.controller.TransactionController;
import org.project.dto.TransactionDTO;
import org.project.security.JwtService;
import org.project.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransactionController.class)
@AutoConfigureMockMvc(addFilters = false)
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private TransactionService transactionService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllTransactions_ShouldReturnTransactions() throws Exception {
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setId(1L);
        transactionDTO.setAmount(1000.0);
        transactionDTO.setType("DEPOSIT");

        Mockito.when(transactionService.getAllTransactions()).thenReturn(Collections.singletonList(transactionDTO));

        mockMvc.perform(get("/api/v1/transactions"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].type", is("DEPOSIT")));
    }

    @Test
    @WithMockUser
    void createTransaction_ShouldReturnCreatedTransaction() throws Exception {
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setBrokerAccountId(1L);
        transactionDTO.setAmount(500.0);
        transactionDTO.setType("WITHDRAWAL");

        TransactionDTO savedTransaction = new TransactionDTO();
        savedTransaction.setId(1L);
        savedTransaction.setBrokerAccountId(transactionDTO.getBrokerAccountId());
        savedTransaction.setAmount(transactionDTO.getAmount());
        savedTransaction.setType(transactionDTO.getType());
        savedTransaction.setTransactionDate(LocalDateTime.now());

        Mockito.when(transactionService.createTransaction(Mockito.any(TransactionDTO.class)))
                .thenReturn(savedTransaction);

        mockMvc.perform(post("/api/v1/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transactionDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.type", is("WITHDRAWAL")));
    }
}