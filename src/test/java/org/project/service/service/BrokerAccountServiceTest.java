package org.project.service.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.project.dto.BrokerAccountDTO;
import org.project.model.BrokerAccount;
import org.project.model.Client;
import org.project.repository.BrokerAccountRepository;
import org.project.repository.ClientRepository;
import org.project.service.BrokerAccountService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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

    @Test
    void getBrokerAccountById_WhenAccountExists_ShouldReturnAccountDTO() {
        // Arrange
        Long accountId = 1L;
        BrokerAccount account = new BrokerAccount();
        account.setId(accountId);
        BrokerAccountDTO accountDTO = new BrokerAccountDTO();
        accountDTO.setId(accountId);

        when(brokerAccountRepository.findByIdAndDeletedFalse(accountId)).thenReturn(Optional.of(account));
        when(modelMapper.map(account, BrokerAccountDTO.class)).thenReturn(accountDTO);

        // Act
        BrokerAccountDTO result = brokerAccountService.getBrokerAccountById(accountId);

        // Assert
        assertNotNull(result);
        assertEquals(accountId, result.getId());
        verify(brokerAccountRepository, times(1)).findByIdAndDeletedFalse(accountId);
    }

    @Test
    void createBrokerAccount_WhenClientExists_ShouldReturnCreatedAccount() {
        // Arrange
        Long clientId = 1L;
        Client client = new Client();
        client.setId(clientId);

        BrokerAccountDTO accountDTO = new BrokerAccountDTO();
        accountDTO.setClientId(clientId);
        accountDTO.setAccountNumber("ACC123");
        accountDTO.setBalance(1000.0);

        BrokerAccount account = new BrokerAccount();
        account.setClient(client);
        account.setAccountNumber(accountDTO.getAccountNumber());
        account.setBalance(accountDTO.getBalance());

        BrokerAccount savedAccount = new BrokerAccount();
        savedAccount.setId(1L);
        savedAccount.setClient(client);
        savedAccount.setAccountNumber(account.getAccountNumber());
        savedAccount.setBalance(account.getBalance());

        BrokerAccountDTO savedAccountDTO = new BrokerAccountDTO();
        savedAccountDTO.setId(1L);
        savedAccountDTO.setClientId(clientId);
        savedAccountDTO.setAccountNumber(accountDTO.getAccountNumber());
        savedAccountDTO.setBalance(accountDTO.getBalance());

        when(clientRepository.findByIdAndDeletedFalse(clientId)).thenReturn(Optional.of(client));
        when(modelMapper.map(accountDTO, BrokerAccount.class)).thenReturn(account);
        when(brokerAccountRepository.save(account)).thenReturn(savedAccount);
        when(modelMapper.map(savedAccount, BrokerAccountDTO.class)).thenReturn(savedAccountDTO);

        // Act
        BrokerAccountDTO result = brokerAccountService.createBrokerAccount(accountDTO);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("ACC123", result.getAccountNumber());
        verify(clientRepository, times(1)).findByIdAndDeletedFalse(clientId);
        verify(brokerAccountRepository, times(1)).save(account);
    }
}