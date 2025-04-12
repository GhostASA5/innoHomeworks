package org.project.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.project.dto.TransactionDTO;
import org.project.exception.EntityNotFoundException;
import org.project.model.BrokerAccount;
import org.project.model.Transaction;
import org.project.repository.BrokerAccountRepository;
import org.project.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final BrokerAccountRepository brokerAccountRepository;
    private final ModelMapper modelMapper;

    public List<TransactionDTO> getAllTransactions() {
        return transactionRepository.findAllByDeletedFalse().stream()
                .map(transaction -> modelMapper.map(transaction, TransactionDTO.class))
                .collect(Collectors.toList());
    }

    public TransactionDTO getTransactionById(Long id) {
        Transaction transaction = transactionRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new EntityNotFoundException("Transaction not found with id: " + id));
        return modelMapper.map(transaction, TransactionDTO.class);
    }

    public List<TransactionDTO> getTransactionsByBrokerAccountId(Long brokerAccountId) {
        return transactionRepository.findAllByBrokerAccountIdAndDeletedFalse(brokerAccountId).stream()
                .map(transaction -> modelMapper.map(transaction, TransactionDTO.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public TransactionDTO createTransaction(TransactionDTO transactionDTO) {
        BrokerAccount account = brokerAccountRepository.findByIdAndDeletedFalse(transactionDTO.getBrokerAccountId())
                .orElseThrow(() -> new EntityNotFoundException("Broker account not found with id: " + transactionDTO.getBrokerAccountId()));

        Transaction transaction = modelMapper.map(transactionDTO, Transaction.class);
        transaction.setBrokerAccount(account);

        if (transaction.getTransactionDate() == null) {
            transaction.setTransactionDate(LocalDateTime.now());
        }

        transaction.setDeleted(false);

        // Update account balance based on transaction type
        if ("DEPOSIT".equalsIgnoreCase(transaction.getType())) {
            account.setBalance(account.getBalance() + transaction.getAmount());
        } else if ("WITHDRAWAL".equalsIgnoreCase(transaction.getType())) {
            if (account.getBalance() < transaction.getAmount()) {
                throw new IllegalArgumentException("Insufficient funds for withdrawal");
            }
            account.setBalance(account.getBalance() - transaction.getAmount());
        }

        brokerAccountRepository.save(account);
        Transaction savedTransaction = transactionRepository.save(transaction);
        return modelMapper.map(savedTransaction, TransactionDTO.class);
    }

    @Transactional
    public void deleteTransaction(Long id) {
        Transaction transaction = transactionRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new EntityNotFoundException("Transaction not found with id: " + id));
        transaction.setDeleted(true);
        transactionRepository.save(transaction);
    }
}