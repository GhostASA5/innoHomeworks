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
import org.project.model.Role;
import org.project.repository.ClientRepository;
import org.project.service.ClientService;
import org.springframework.cache.CacheManager;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private CacheManager cacheManager;

    @InjectMocks
    private ClientService clientService;

    private final Client testClient = new Client(1L, "Иван", "Иванов", "ivan@example.com", "+79123456789", Role.USER, false);
    private final ClientDTO testClientDTO = new ClientDTO(1L, "Иван", "ivan@example.com", "+79123456789");

    @Test
    void getAllClients_ReturnsActiveClients() {
        // Arrange
        when(clientRepository.findAllByDeletedFalse()).thenReturn(List.of(testClient));
        when(modelMapper.map(testClient, ClientDTO.class)).thenReturn(testClientDTO);

        // Act
        List<ClientDTO> result = clientService.getAllClients();

        // Assert
        assertEquals(1, result.size());
        assertEquals(testClientDTO, result.get(0));
        verify(clientRepository).findAllByDeletedFalse();
        verify(modelMapper).map(testClient, ClientDTO.class);
    }

    @Test
    void getClientById_ExistingId_ReturnsClient() {
        // Arrange
        when(clientRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.of(testClient));
        when(modelMapper.map(testClient, ClientDTO.class)).thenReturn(testClientDTO);

        // Act
        ClientDTO result = clientService.getClientById(1L);

        // Assert
        assertEquals(testClientDTO, result);
        verify(clientRepository).findByIdAndDeletedFalse(1L);
        verify(modelMapper).map(testClient, ClientDTO.class);
    }

    @Test
    void getClientById_NonExistingId_ThrowsException() {
        // Arrange
        when(clientRepository.findByIdAndDeletedFalse(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> clientService.getClientById(99L));
        verify(clientRepository).findByIdAndDeletedFalse(99L);
    }

    @Test
    void getClientByEmail_ExistingEmail_ReturnsClient() {
        // Arrange
        when(clientRepository.findByEmail("ivan@example.com")).thenReturn(Optional.of(testClient));

        // Act
        Client result = clientService.getClientByEmail("ivan@example.com");

        // Assert
        assertEquals(testClient, result);
        verify(clientRepository).findByEmail("ivan@example.com");
    }

    @Test
    void getClientByEmail_NonExistingEmail_ThrowsException() {
        // Arrange
        when(clientRepository.findByEmail("nonexisting@example.com")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class,
                () -> clientService.getClientByEmail("nonexisting@example.com"));
        verify(clientRepository).findByEmail("nonexisting@example.com");
    }

    @Test
    void createClient_ValidData_ReturnsCreatedClient() {
        // Arrange
        when(modelMapper.map(testClientDTO, Client.class)).thenReturn(testClient);
        when(clientRepository.save(testClient)).thenReturn(testClient);
        when(modelMapper.map(testClient, ClientDTO.class)).thenReturn(testClientDTO);

        // Act
        ClientDTO result = clientService.createClient(testClientDTO);

        // Assert
        assertEquals(testClientDTO, result);
        assertFalse(testClient.isDeleted());
        verify(modelMapper).map(testClientDTO, Client.class);
        verify(clientRepository).save(testClient);
        verify(modelMapper).map(testClient, ClientDTO.class);
    }

    @Test
    void updateClient_NonExistingId_ThrowsException() {
        // Arrange
        when(clientRepository.findByIdAndDeletedFalse(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class,
                () -> clientService.updateClient(99L, testClientDTO));
        verify(clientRepository).findByIdAndDeletedFalse(99L);
    }

    @Test
    void deleteClient_ExistingId_SetsDeletedFlag() {
        // Arrange
        when(clientRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.of(testClient));
        when(clientRepository.save(testClient)).thenReturn(testClient);

        // Act
        clientService.deleteClient(1L);

        // Assert
        assertTrue(testClient.isDeleted());
        verify(clientRepository).findByIdAndDeletedFalse(1L);
        verify(clientRepository).save(testClient);
    }

    @Test
    void deleteClient_NonExistingId_ThrowsException() {
        // Arrange
        when(clientRepository.findByIdAndDeletedFalse(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class,
                () -> clientService.deleteClient(99L));
        verify(clientRepository).findByIdAndDeletedFalse(99L);
    }
}