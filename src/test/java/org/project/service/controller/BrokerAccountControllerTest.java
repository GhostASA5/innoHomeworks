package org.project.service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.project.controller.BrokerAccountController;
import org.project.dto.BrokerAccountDTO;
import org.project.security.JwtService;
import org.project.service.BrokerAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BrokerAccountController.class)
@AutoConfigureMockMvc(addFilters = false)
class BrokerAccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private BrokerAccountService brokerAccountService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllBrokerAccounts_ShouldReturnAccounts() throws Exception {
        BrokerAccountDTO accountDTO = new BrokerAccountDTO();
        accountDTO.setId(1L);
        accountDTO.setAccountNumber("ACC123");
        accountDTO.setBalance(1000.0);

        Mockito.when(brokerAccountService.getAllBrokerAccounts()).thenReturn(Collections.singletonList(accountDTO));

        mockMvc.perform(get("/api/v1/accounts"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].accountNumber", is("ACC123")));
    }

    @Test
    @WithMockUser
    void createBrokerAccount_ShouldReturnCreatedAccount() throws Exception {
        BrokerAccountDTO accountDTO = new BrokerAccountDTO();
        accountDTO.setClientId(1L);
        accountDTO.setAccountNumber("NEWACC");
        accountDTO.setBalance(500.0);

        BrokerAccountDTO savedAccount = new BrokerAccountDTO();
        savedAccount.setId(1L);
        savedAccount.setClientId(accountDTO.getClientId());
        savedAccount.setAccountNumber(accountDTO.getAccountNumber());
        savedAccount.setBalance(accountDTO.getBalance());

        Mockito.when(brokerAccountService.createBrokerAccount(Mockito.any(BrokerAccountDTO.class))).thenReturn(savedAccount);

        mockMvc.perform(post("/api/v1/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.accountNumber", is("NEWACC")));
    }
}