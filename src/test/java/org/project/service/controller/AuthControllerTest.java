package org.project.service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.project.controller.AuthController;
import org.project.dto.AuthRequest;
import org.project.dto.AuthResponse;
import org.project.dto.RegisterRequest;
import org.project.security.JwtService;
import org.project.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private AuthService authService;

    @Test
    void register_ValidRequest_ReturnsAuthResponse() throws Exception {
        RegisterRequest request = new RegisterRequest(
                "John", "john.doe@example.com", "89876544321", "password123");

        AuthResponse response = new AuthResponse("jwt.token.example");

        Mockito.when(authService.register(request)).thenReturn(response);

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", is(response.getToken())));
    }

    @Test
    void register_InvalidRequest_ReturnsBadRequest() throws Exception {
        RegisterRequest invalidRequest = new RegisterRequest(
                "", "", "not-an-email", "short");

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void authenticate_ValidCredentials_ReturnsAuthResponse() throws Exception {
        AuthRequest request = new AuthRequest(
                "john.doe@example.com", "password123");

        AuthResponse response = new AuthResponse("jwt.token.example");

        Mockito.when(authService.authenticate(request)).thenReturn(response);

        mockMvc.perform(post("/api/v1/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", is(response.getToken())));
    }

    @Test
    void authenticate_InvalidRequest_ReturnsBadRequest() throws Exception {
        AuthRequest invalidRequest = new AuthRequest("", "");

        mockMvc.perform(post("/api/v1/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }
}