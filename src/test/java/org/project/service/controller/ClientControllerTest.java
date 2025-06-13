package org.project.service.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.project.controller.ClientController;
import org.project.dto.ClientDTO;
import org.project.security.JwtAuthenticationFilter;
import org.project.security.JwtService;
import org.project.service.ClientService;
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

@WebMvcTest(ClientController.class)
@AutoConfigureMockMvc(addFilters = false)
class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private ClientService clientService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllClients_ShouldReturnClients() throws Exception {
        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setId(1L);
        clientDTO.setName("Test Client");
        clientDTO.setEmail("test@example.com");
        clientDTO.setPhone("+1234567890");

        Mockito.when(clientService.getAllClients()).thenReturn(Collections.singletonList(clientDTO));

        mockMvc.perform(get("/api/v1/clients"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Test Client")));
    }

    @Test
    @WithMockUser
    void createClient_ShouldReturnCreatedClient() throws Exception {
        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setName("New Client");
        clientDTO.setEmail("new@example.com");
        clientDTO.setPhone("+9876543210");

        ClientDTO savedClient = new ClientDTO();
        savedClient.setId(1L);
        savedClient.setName(clientDTO.getName());
        savedClient.setEmail(clientDTO.getEmail());
        savedClient.setPhone(clientDTO.getPhone());

        Mockito.when(clientService.createClient(Mockito.any(ClientDTO.class))).thenReturn(savedClient);

        mockMvc.perform(post("/api/v1/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clientDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("New Client")));
    }
}