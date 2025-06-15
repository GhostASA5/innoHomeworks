package org.project.service.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.project.dto.BrokerAccountDTO;
import org.project.exception.EntityNotFoundException;
import org.project.model.BrokerAccount;
import org.project.model.Client;
import org.project.model.Role;
import org.project.repository.BrokerAccountRepository;
import org.project.repository.ClientRepository;
import org.project.service.BrokerAccountService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
class BrokerAccountServiceTest {

    @Mock
    private BrokerAccountRepository brokerAccountRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private BrokerAccountService brokerAccountService;

    private final Client testClient = new Client(1L, "Иван", "Иванов", "ivan@example.com", "+79123456789", Role.USER, false);
    private final BrokerAccount testAccount = new BrokerAccount(1L, testClient, "ACC123", 10000.0, false);
    private final BrokerAccountDTO testAccountDTO = new BrokerAccountDTO(1L, 1L, "ACC123", 10000.0);

    @Test
    void getAllBrokerAccounts_ReturnsActiveAccounts() {
        // Arrange
        when(brokerAccountRepository.findAllByDeletedFalse()).thenReturn(List.of(testAccount));
        when(modelMapper.map(testAccount, BrokerAccountDTO.class)).thenReturn(testAccountDTO);

        // Act
        List<BrokerAccountDTO> result = brokerAccountService.getAllBrokerAccounts();

        // Assert
        assertEquals(1, result.size());
        assertEquals(testAccountDTO, result.get(0));
        verify(brokerAccountRepository).findAllByDeletedFalse();
        verify(modelMapper).map(testAccount, BrokerAccountDTO.class);
    }

    @Test
    void getBrokerAccountById_ExistingId_ReturnsAccount() {
        // Arrange
        when(brokerAccountRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.of(testAccount));
        when(modelMapper.map(testAccount, BrokerAccountDTO.class)).thenReturn(testAccountDTO);

        // Act
        BrokerAccountDTO result = brokerAccountService.getBrokerAccountById(1L);

        // Assert
        assertEquals(testAccountDTO, result);
        verify(brokerAccountRepository).findByIdAndDeletedFalse(1L);
        verify(modelMapper).map(testAccount, BrokerAccountDTO.class);
    }

    @Test
    void getBrokerAccountById_NonExistingId_ThrowsException() {
        // Arrange
        when(brokerAccountRepository.findByIdAndDeletedFalse(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> brokerAccountService.getBrokerAccountById(99L));
        verify(brokerAccountRepository).findByIdAndDeletedFalse(99L);
    }

    @Test
    void getBrokerAccountsByClientId_ReturnsClientAccounts() {
        // Arrange
        when(brokerAccountRepository.findAllByClientIdAndDeletedFalse(1L)).thenReturn(List.of(testAccount));
        when(modelMapper.map(testAccount, BrokerAccountDTO.class)).thenReturn(testAccountDTO);

        // Act
        List<BrokerAccountDTO> result = brokerAccountService.getBrokerAccountsByClientId(1L);

        // Assert
        assertEquals(1, result.size());
        assertEquals(testAccountDTO, result.get(0));
        verify(brokerAccountRepository).findAllByClientIdAndDeletedFalse(1L);
        verify(modelMapper).map(testAccount, BrokerAccountDTO.class);
    }

    @Test
    @Transactional
    void createBrokerAccount_ValidData_ReturnsCreatedAccount() {
        // Arrange
        BrokerAccountDTO newAccountDTO = new BrokerAccountDTO(1L, 1L, "ACC123", 10000.0);
        BrokerAccount newAccount = new BrokerAccount(1L, testClient, "ACC123", 10000.0, false);

        when(clientRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.of(testClient));
        when(modelMapper.map(newAccountDTO, BrokerAccount.class)).thenReturn(newAccount);
        when(brokerAccountRepository.save(newAccount)).thenReturn(testAccount);
        when(modelMapper.map(testAccount, BrokerAccountDTO.class)).thenReturn(testAccountDTO);

        // Act
        BrokerAccountDTO result = brokerAccountService.createBrokerAccount(newAccountDTO);

        // Assert
        assertEquals(testAccountDTO, result);
        verify(clientRepository).findByIdAndDeletedFalse(1L);
        verify(brokerAccountRepository).save(newAccount);
        verify(modelMapper).map(testAccount, BrokerAccountDTO.class);
    }

    @Test
    @Transactional
    void updateBrokerAccount_NonExistingAccount_ThrowsException() {
        // Arrange
        BrokerAccountDTO updatedDTO = new BrokerAccountDTO(1L, 1L, "ACC123", 10000.0);
        when(brokerAccountRepository.findByIdAndDeletedFalse(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> brokerAccountService.updateBrokerAccount(99L, updatedDTO));
        verify(brokerAccountRepository).findByIdAndDeletedFalse(99L);
        verify(clientRepository, never()).findByIdAndDeletedFalse(any());
        verify(brokerAccountRepository, never()).save(any());
    }

    @Test
    @Transactional
    void deleteBrokerAccount_ExistingAccount_SetsDeletedFlag() {
        // Arrange
        when(brokerAccountRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.of(testAccount));

        // Act
        brokerAccountService.deleteBrokerAccount(1L);

        // Assert
        assertTrue(testAccount.isDeleted());
        verify(brokerAccountRepository).findByIdAndDeletedFalse(1L);
        verify(brokerAccountRepository).save(testAccount);
    }

    @Test
    @Transactional
    void deleteBrokerAccount_NonExistingAccount_ThrowsException() {
        // Arrange
        when(brokerAccountRepository.findByIdAndDeletedFalse(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> brokerAccountService.deleteBrokerAccount(99L));
        verify(brokerAccountRepository).findByIdAndDeletedFalse(99L);
        verify(brokerAccountRepository, never()).save(any());
    }
}