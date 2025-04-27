package org.project.service.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.project.dto.ClientDTO;
import org.project.exception.EntityNotFoundException;
import org.project.model.Client;
import org.project.repository.ClientRepository;
import org.project.service.ClientService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ClientService clientService;

    @Test
    void getClientById_WhenClientExists_ShouldReturnClientDTO() {
        // Arrange
        Long clientId = 1L;
        Client client = new Client();
        client.setId(clientId);
        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setId(clientId);

        when(clientRepository.findByIdAndDeletedFalse(clientId)).thenReturn(Optional.of(client));
        when(modelMapper.map(client, ClientDTO.class)).thenReturn(clientDTO);

        ClientDTO result = clientService.getClientById(clientId);

        assertNotNull(result);
        assertEquals(clientId, result.getId());
        verify(clientRepository, times(1)).findByIdAndDeletedFalse(clientId);
    }

    @Test
    void getClientById_WhenClientNotExists_ShouldThrowException() {
        Long clientId = 1L;
        when(clientRepository.findByIdAndDeletedFalse(clientId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> clientService.getClientById(clientId));
    }
}