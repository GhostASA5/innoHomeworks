package org.project.service.controller;


import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.project.controller.ClientController;
import org.project.dto.ClientDTO;
import org.project.exception.EntityNotFoundException;
import org.project.security.JwtService;
import org.project.service.ClientSecurityService;
import org.project.service.ClientService;
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

@WebMvcTest(ClientController.class)
@AutoConfigureMockMvc(addFilters = false)
class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private ClientService clientService;

    @MockBean
    private ClientSecurityService clientSecurityService;

    private final ClientDTO testClient = new ClientDTO(
            1L, "Иван", "ivan@example.com", "+79123456789");

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllClients_AdminAccess_ReturnsClients() throws Exception {
        // Arrange
        List<ClientDTO> clients = Collections.singletonList(testClient);
        Mockito.when(clientService.getAllClients()).thenReturn(clients);

        // Act & Assert
        mockMvc.perform(get("/api/v1/clients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(testClient.getId().intValue())));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getClientById_AdminAccess_ReturnsClient() throws Exception {
        // Arrange
        Mockito.when(clientService.getClientById(1L)).thenReturn(testClient);

        // Act & Assert
        mockMvc.perform(get("/api/v1/clients/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testClient.getId().intValue())));
    }



    @Test
    @WithMockUser(roles = "ADMIN")
    void getClientById_NotFound_ReturnsNotFound() throws Exception {
        // Arrange
        Mockito.when(clientService.getClientById(99L))
                .thenThrow(new EntityNotFoundException("Client not found"));

        // Act & Assert
        mockMvc.perform(get("/api/v1/clients/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createClient_ValidRequest_ReturnsCreated() throws Exception {
        // Arrange
        Mockito.when(clientService.createClient(testClient)).thenReturn(testClient);

        // Act & Assert
        mockMvc.perform(post("/api/v1/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testClient)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(testClient.getId().intValue())));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateClient_AdminAccess_ReturnsUpdatedClient() throws Exception {
        // Arrange
        Mockito.when(clientService.updateClient(1L, testClient)).thenReturn(testClient);

        // Act & Assert
        mockMvc.perform(put("/api/v1/clients/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testClient)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testClient.getId().intValue())));
    }

    @Test
    @WithMockUser
    void updateClient_OwnerAccess_ReturnsUpdatedClient() throws Exception {
        // Arrange
        Mockito.when(clientSecurityService.isClientOwner(1L, null)).thenReturn(true);
        Mockito.when(clientService.updateClient(1L, testClient)).thenReturn(testClient);

        // Act & Assert
        mockMvc.perform(put("/api/v1/clients/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testClient)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is(testClient.getEmail())));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteClient_AdminAccess_ReturnsNoContent() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/api/v1/clients/1"))
                .andExpect(status().isNoContent());

        Mockito.verify(clientService).deleteClient(1L);
    }

    @Test
    @WithMockUser
    void deleteClient_OwnerAccess_ReturnsNoContent() throws Exception {
        // Arrange
        Mockito.when(clientSecurityService.isClientOwner(1L, null)).thenReturn(true);

        // Act & Assert
        mockMvc.perform(delete("/api/v1/clients/1"))
                .andExpect(status().isNoContent());

        Mockito.verify(clientService).deleteClient(1L);
    }
}