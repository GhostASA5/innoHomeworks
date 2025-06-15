package org.project.service.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.project.controller.BrokerAccountController;
import org.project.dto.BrokerAccountDTO;
import org.project.security.JwtService;
import org.project.service.BrokerAccountService;
import org.project.service.ClientSecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@WebMvcTest(BrokerAccountController.class)
@AutoConfigureMockMvc(addFilters = false)
class BrokerAccountControllerTest {

    @MockBean
    private JwtService jwtService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BrokerAccountService brokerAccountService;

    @MockBean
    private ClientSecurityService clientSecurityService;

    private final BrokerAccountDTO testAccount = new BrokerAccountDTO(
            1L, 1L, "ABC",  1000.0);

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllBrokerAccounts_AdminAccess_ReturnsAccounts() throws Exception {
        // Arrange
        List<BrokerAccountDTO> accounts = Collections.singletonList(testAccount);
        Mockito.when(brokerAccountService.getAllBrokerAccounts()).thenReturn(accounts);

        // Act & Assert
        mockMvc.perform(get("/api/v1/accounts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(testAccount.getId().intValue())));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getBrokerAccountById_AdminAccess_ReturnsAccount() throws Exception {
        // Arrange
        Mockito.when(brokerAccountService.getBrokerAccountById(1L)).thenReturn(testAccount);

        // Act & Assert
        mockMvc.perform(get("/api/v1/accounts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testAccount.getId().intValue())));
    }

    @Test
    @WithMockUser
    void getBrokerAccountById_OwnerAccess_ReturnsAccount() throws Exception {
        // Arrange
        Mockito.when(clientSecurityService.isAccountOwner(1L, null)).thenReturn(true);
        Mockito.when(brokerAccountService.getBrokerAccountById(1L)).thenReturn(testAccount);

        // Act & Assert
        mockMvc.perform(get("/api/v1/accounts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testAccount.getId().intValue())));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getBrokerAccountsByClientId_AdminAccess_ReturnsAccounts() throws Exception {
        // Arrange
        List<BrokerAccountDTO> accounts = Collections.singletonList(testAccount);
        Mockito.when(brokerAccountService.getBrokerAccountsByClientId(1L)).thenReturn(accounts);

        // Act & Assert
        mockMvc.perform(get("/api/v1/accounts/client/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].clientId", is(testAccount.getClientId().intValue())));
    }

    @Test
    @WithMockUser
    void getBrokerAccountsByClientId_OwnerAccess_ReturnsAccounts() throws Exception {
        // Arrange
        Mockito.when(clientSecurityService.isClientOwner(1L, null)).thenReturn(true);
        List<BrokerAccountDTO> accounts = Collections.singletonList(testAccount);
        Mockito.when(brokerAccountService.getBrokerAccountsByClientId(1L)).thenReturn(accounts);

        // Act & Assert
        mockMvc.perform(get("/api/v1/accounts/client/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    @WithMockUser
    void createBrokerAccount_ValidRequest_ReturnsCreated() throws Exception {
        // Arrange
        Mockito.when(clientSecurityService.isClientOwner(1L, null)).thenReturn(true);
        Mockito.when(brokerAccountService.createBrokerAccount(testAccount)).thenReturn(testAccount);

        // Act & Assert
        mockMvc.perform(post("/api/v1/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testAccount)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(testAccount.getId().intValue())));
    }


    @Test
    @WithMockUser(roles = "ADMIN")
    void updateBrokerAccount_AdminAccess_ReturnsUpdatedAccount() throws Exception {
        // Arrange
        Mockito.when(brokerAccountService.updateBrokerAccount(1L, testAccount)).thenReturn(testAccount);

        // Act & Assert
        mockMvc.perform(put("/api/v1/accounts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testAccount)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testAccount.getId().intValue())));
    }

    @Test
    @WithMockUser
    void updateBrokerAccount_OwnerAccess_ReturnsUpdatedAccount() throws Exception {
        // Arrange
        Mockito.when(clientSecurityService.isAccountOwner(1L, null)).thenReturn(true);
        Mockito.when(brokerAccountService.updateBrokerAccount(1L, testAccount)).thenReturn(testAccount);

        // Act & Assert
        mockMvc.perform(put("/api/v1/accounts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testAccount)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testAccount.getId().intValue())));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteBrokerAccount_AdminAccess_ReturnsNoContent() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/api/v1/accounts/1"))
                .andExpect(status().isNoContent());

        Mockito.verify(brokerAccountService).deleteBrokerAccount(1L);
    }

    @Test
    @WithMockUser
    void deleteBrokerAccount_OwnerAccess_ReturnsNoContent() throws Exception {
        // Arrange
        Mockito.when(clientSecurityService.isAccountOwner(1L, null)).thenReturn(true);

        // Act & Assert
        mockMvc.perform(delete("/api/v1/accounts/1"))
                .andExpect(status().isNoContent());

        Mockito.verify(brokerAccountService).deleteBrokerAccount(1L);
    }
}