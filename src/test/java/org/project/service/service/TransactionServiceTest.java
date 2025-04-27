package org.project.service.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.project.dto.TransactionDTO;
import org.project.model.BrokerAccount;
import org.project.model.Transaction;
import org.project.repository.BrokerAccountRepository;
import org.project.repository.TransactionRepository;
import org.project.service.TransactionService;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private BrokerAccountRepository brokerAccountRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    void createTransaction_WhenDeposit_ShouldIncreaseBalance() {
        // Arrange
        Long accountId = 1L;
        BrokerAccount account = new BrokerAccount();
        account.setId(accountId);
        account.setBalance(1000.0);

        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setBrokerAccountId(accountId);
        transactionDTO.setAmount(500.0);
        transactionDTO.setType("DEPOSIT");

        Transaction transaction = new Transaction();
        transaction.setBrokerAccount(account);
        transaction.setAmount(transactionDTO.getAmount());
        transaction.setType(transactionDTO.getType());
        transaction.setTransactionDate(LocalDateTime.now());

        Transaction savedTransaction = new Transaction();
        savedTransaction.setId(1L);
        savedTransaction.setBrokerAccount(account);
        savedTransaction.setAmount(transaction.getAmount());
        savedTransaction.setType(transaction.getType());
        savedTransaction.setTransactionDate(transaction.getTransactionDate());

        TransactionDTO savedTransactionDTO = new TransactionDTO();
        savedTransactionDTO.setId(1L);
        savedTransactionDTO.setBrokerAccountId(accountId);
        savedTransactionDTO.setAmount(transactionDTO.getAmount());
        savedTransactionDTO.setType(transactionDTO.getType());

        when(brokerAccountRepository.findByIdAndDeletedFalse(accountId)).thenReturn(Optional.of(account));
        when(modelMapper.map(transactionDTO, Transaction.class)).thenReturn(transaction);
        when(transactionRepository.save(transaction)).thenReturn(savedTransaction);
        when(modelMapper.map(savedTransaction, TransactionDTO.class)).thenReturn(savedTransactionDTO);

        // Act
        TransactionDTO result = transactionService.createTransaction(transactionDTO);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(1500.0, account.getBalance());
        verify(brokerAccountRepository, times(1)).findByIdAndDeletedFalse(accountId);
        verify(transactionRepository, times(1)).save(transaction);
    }

    @Test
    void createTransaction_WhenWithdrawalWithInsufficientFunds_ShouldThrowException() {
        // Arrange
        Long accountId = 1L;
        BrokerAccount account = new BrokerAccount();
        account.setId(accountId);
        account.setBalance(100.0);

        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setBrokerAccountId(accountId);
        transactionDTO.setAmount(500.0);
        transactionDTO.setType("WITHDRAWAL");

        Transaction transaction = new Transaction();
        transaction.setBrokerAccount(account);
        transaction.setAmount(transactionDTO.getAmount());
        transaction.setType(transactionDTO.getType());

        when(brokerAccountRepository.findByIdAndDeletedFalse(accountId)).thenReturn(Optional.of(account));
        when(modelMapper.map(transactionDTO, Transaction.class)).thenReturn(transaction);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> transactionService.createTransaction(transactionDTO));
    }
}
