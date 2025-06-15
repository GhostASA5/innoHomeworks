package org.project.service.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.project.dto.TransactionDTO;
import org.project.exception.EntityNotFoundException;
import org.project.model.BrokerAccount;
import org.project.model.Client;
import org.project.model.Role;
import org.project.model.Transaction;
import org.project.repository.BrokerAccountRepository;
import org.project.repository.TransactionRepository;
import org.project.service.TransactionService;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private BrokerAccountRepository brokerAccountRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private KafkaTemplate<String, Transaction> kafkaTemplate;

    @InjectMocks
    private TransactionService transactionService;

    private final Client testClient = new Client(1L, "Иван", "Иванов", "ivan@example.com", "+79123456789", Role.USER, false);
    private final BrokerAccount testAccount = new BrokerAccount(1L, testClient, "ACC123", 10000.0, false);
    private final Transaction testTransaction = new Transaction(1L, testAccount, LocalDateTime.now(), 1000.0, "Test deposit", false);
    private final TransactionDTO testTransactionDTO = new TransactionDTO(1L, 1L, LocalDateTime.now(), 1000.0, "DEPOSIT");

    @Test
    void getAllTransactions_ReturnsActiveTransactions() {
        // Arrange
        when(transactionRepository.findAllByDeletedFalse()).thenReturn(List.of(testTransaction));
        when(modelMapper.map(testTransaction, TransactionDTO.class)).thenReturn(testTransactionDTO);

        // Act
        List<TransactionDTO> result = transactionService.getAllTransactions();

        // Assert
        assertEquals(1, result.size());
        assertEquals(testTransactionDTO, result.get(0));
        verify(transactionRepository).findAllByDeletedFalse();
        verify(modelMapper).map(testTransaction, TransactionDTO.class);
    }

    @Test
    void getTransactionById_ExistingId_ReturnsTransaction() {
        // Arrange
        when(transactionRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.of(testTransaction));
        when(modelMapper.map(testTransaction, TransactionDTO.class)).thenReturn(testTransactionDTO);

        // Act
        TransactionDTO result = transactionService.getTransactionById(1L);

        // Assert
        assertEquals(testTransactionDTO, result);
        verify(transactionRepository).findByIdAndDeletedFalse(1L);
        verify(modelMapper).map(testTransaction, TransactionDTO.class);
    }

    @Test
    void getTransactionById_NonExistingId_ThrowsException() {
        // Arrange
        when(transactionRepository.findByIdAndDeletedFalse(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> transactionService.getTransactionById(99L));
        verify(transactionRepository).findByIdAndDeletedFalse(99L);
    }

    @Test
    void getTransactionsByBrokerAccountId_ReturnsAccountTransactions() {
        // Arrange
        when(transactionRepository.findAllByBrokerAccountIdAndDeletedFalse(1L)).thenReturn(List.of(testTransaction));
        when(modelMapper.map(testTransaction, TransactionDTO.class)).thenReturn(testTransactionDTO);

        // Act
        List<TransactionDTO> result = transactionService.getTransactionsByBrokerAccountId(1L);

        // Assert
        assertEquals(1, result.size());
        assertEquals(testTransactionDTO, result.get(0));
        verify(transactionRepository).findAllByBrokerAccountIdAndDeletedFalse(1L);
        verify(modelMapper).map(testTransaction, TransactionDTO.class);
    }

    @Test
    @Transactional
    void createTransaction_Deposit_UpdatesBalanceAndReturnsTransaction() {
        // Arrange
        TransactionDTO depositDTO = new TransactionDTO(null, 1L, LocalDateTime.now(), 1000.0, "DEPOSIT");
        Transaction deposit = new Transaction(null, testAccount, LocalDateTime.now(), 1000.0, "DEPOSIT", false);

        when(brokerAccountRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.of(testAccount));
        when(modelMapper.map(depositDTO, Transaction.class)).thenReturn(deposit);
        when(transactionRepository.save(deposit)).thenReturn(testTransaction);
        when(modelMapper.map(testTransaction, TransactionDTO.class)).thenReturn(testTransactionDTO);

        // Act
        TransactionDTO result = transactionService.createTransaction(depositDTO);

        // Assert
        assertEquals(testTransactionDTO, result);
        assertEquals(11000.0, testAccount.getBalance());
        verify(brokerAccountRepository).findByIdAndDeletedFalse(1L);
        verify(transactionRepository).save(deposit);
        verify(brokerAccountRepository).save(testAccount);
        verify(kafkaTemplate, never()).send(any(), any());
    }

    @Test
    @Transactional
    void createTransaction_LargeDeposit_SendsKafkaMessage() {
        // Arrange
        TransactionDTO largeDepositDTO = new TransactionDTO(null, 1L, LocalDateTime.now(), 10000000.0, "DEPOSIT");
        Transaction largeDeposit = new Transaction(null, testAccount, LocalDateTime.now(), 10000000.0, "DEPOSIT", false);

        when(brokerAccountRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.of(testAccount));
        when(modelMapper.map(largeDepositDTO, Transaction.class)).thenReturn(largeDeposit);
        when(transactionRepository.save(largeDeposit)).thenReturn(testTransaction);
        when(modelMapper.map(testTransaction, TransactionDTO.class)).thenReturn(testTransactionDTO);

        // Act
        TransactionDTO result = transactionService.createTransaction(largeDepositDTO);

        // Assert
        assertEquals(testTransactionDTO, result);
    }

    @Test
    @Transactional
    void deleteTransaction_ExistingId_SetsDeletedFlag() {
        // Arrange
        when(transactionRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.of(testTransaction));

        // Act
        transactionService.deleteTransaction(1L);

        // Assert
        assertTrue(testTransaction.isDeleted());
        verify(transactionRepository).findByIdAndDeletedFalse(1L);
        verify(transactionRepository).save(testTransaction);
    }

    @Test
    @Transactional
    void deleteTransaction_NonExistingId_ThrowsException() {
        // Arrange
        when(transactionRepository.findByIdAndDeletedFalse(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> transactionService.deleteTransaction(99L));
        verify(transactionRepository).findByIdAndDeletedFalse(99L);
        verify(transactionRepository, never()).save(any());
    }
}
