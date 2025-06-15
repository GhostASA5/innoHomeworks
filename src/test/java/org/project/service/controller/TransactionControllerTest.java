package org.project.service.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.project.controller.TransactionController;
import org.project.dto.TransactionDTO;
import org.project.exception.EntityNotFoundException;
import org.project.security.JwtService;
import org.project.service.ClientSecurityService;
import org.project.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@WebMvcTest(TransactionController.class)
@AutoConfigureMockMvc(addFilters = false)
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private TransactionService transactionService;

    @MockBean
    private ClientSecurityService clientSecurityService;

    private final TransactionDTO testTransaction = new TransactionDTO(
            1L,
            1L,
            LocalDateTime.now(),
            100.0,
            "DEPOSIT");

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllTransactions_AdminAccess_ReturnsTransactions() throws Exception {
        // Arrange
        List<TransactionDTO> transactions = Collections.singletonList(testTransaction);
        Mockito.when(transactionService.getAllTransactions()).thenReturn(transactions);

        // Act & Assert
        mockMvc.perform(get("/api/v1/transactions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(testTransaction.getId().intValue())));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getTransactionById_AdminAccess_ReturnsTransaction() throws Exception {
        // Arrange
        Mockito.when(transactionService.getTransactionById(1L)).thenReturn(testTransaction);

        // Act & Assert
        mockMvc.perform(get("/api/v1/transactions/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testTransaction.getId().intValue())));
    }

    @Test
    @WithMockUser
    void getTransactionById_OwnerAccess_ReturnsTransaction() throws Exception {
        // Arrange
        Mockito.when(clientSecurityService.isTransactionOwner(1L, null)).thenReturn(true);
        Mockito.when(transactionService.getTransactionById(1L)).thenReturn(testTransaction);

        // Act & Assert
        mockMvc.perform(get("/api/v1/transactions/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type", is(testTransaction.getType())));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getTransactionById_NotFound_ReturnsNotFound() throws Exception {
        // Arrange
        Mockito.when(transactionService.getTransactionById(99L))
                .thenThrow(new EntityNotFoundException("Transaction not found"));

        // Act & Assert
        mockMvc.perform(get("/api/v1/transactions/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getTransactionsByBrokerAccountId_AdminAccess_ReturnsTransactions() throws Exception {
        // Arrange
        List<TransactionDTO> transactions = Collections.singletonList(testTransaction);
        Mockito.when(transactionService.getTransactionsByBrokerAccountId(1L)).thenReturn(transactions);

        // Act & Assert
        mockMvc.perform(get("/api/v1/transactions/account/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].brokerAccountId", is(testTransaction.getBrokerAccountId().intValue())));
    }

    @Test
    @WithMockUser
    void getTransactionsByBrokerAccountId_OwnerAccess_ReturnsTransactions() throws Exception {
        // Arrange
        Mockito.when(clientSecurityService.isAccountOwner(1L, null)).thenReturn(true);
        List<TransactionDTO> transactions = Collections.singletonList(testTransaction);
        Mockito.when(transactionService.getTransactionsByBrokerAccountId(1L)).thenReturn(transactions);

        // Act & Assert
        mockMvc.perform(get("/api/v1/transactions/account/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createTransaction_AdminAccess_ReturnsCreated() throws Exception {
        // Arrange
        Mockito.when(transactionService.createTransaction(testTransaction)).thenReturn(testTransaction);

        // Act & Assert
        mockMvc.perform(post("/api/v1/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testTransaction)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(testTransaction.getId().intValue())));
    }

    @Test
    @WithMockUser
    void createTransaction_OwnerAccess_ReturnsCreated() throws Exception {
        // Arrange
        Mockito.when(clientSecurityService.isAccountOwner(1L, null)).thenReturn(true);
        Mockito.when(transactionService.createTransaction(testTransaction)).thenReturn(testTransaction);

        // Act & Assert
        mockMvc.perform(post("/api/v1/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testTransaction)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createTransaction_AccountNotFound_ReturnsNotFound() throws Exception {
        // Arrange
        Mockito.when(transactionService.createTransaction(testTransaction))
                .thenThrow(new EntityNotFoundException("Account not found"));

        // Act & Assert
        mockMvc.perform(post("/api/v1/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testTransaction)))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteTransaction_AdminAccess_ReturnsNoContent() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/api/v1/transactions/1"))
                .andExpect(status().isNoContent());

        Mockito.verify(transactionService).deleteTransaction(1L);
    }

    @Test
    @WithMockUser
    void deleteTransaction_OwnerAccess_ReturnsNoContent() throws Exception {
        // Arrange
        Mockito.when(clientSecurityService.isTransactionOwner(1L, null)).thenReturn(true);

        // Act & Assert
        mockMvc.perform(delete("/api/v1/transactions/1"))
                .andExpect(status().isNoContent());

        Mockito.verify(transactionService).deleteTransaction(1L);
    }

}