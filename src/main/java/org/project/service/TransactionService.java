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

/**
 * Сервис для работы с транзакциями брокерских счетов.
 * Предоставляет операции для управления финансовыми транзакциями.
 */
@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final BrokerAccountRepository brokerAccountRepository;
    private final ModelMapper modelMapper;

    /**
     * Получает список всех активных транзакций.
     *
     * @return список DTO транзакций
     */
    public List<TransactionDTO> getAllTransactions() {
        return transactionRepository.findAllByDeletedFalse().stream()
                .map(transaction -> modelMapper.map(transaction, TransactionDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * Получает транзакцию по идентификатору.
     *
     * @param id идентификатор транзакции
     * @return DTO транзакции
     * @throws EntityNotFoundException если транзакция не найдена
     */
    public TransactionDTO getTransactionById(Long id) {
        Transaction transaction = transactionRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new EntityNotFoundException("Transaction not found with id: " + id));
        return modelMapper.map(transaction, TransactionDTO.class);
    }

    /**
     * Получает список транзакций по идентификатору брокерского счета.
     *
     * @param brokerAccountId идентификатор брокерского счета
     * @return список DTO транзакций
     */
    public List<TransactionDTO> getTransactionsByBrokerAccountId(Long brokerAccountId) {
        return transactionRepository.findAllByBrokerAccountIdAndDeletedFalse(brokerAccountId).stream()
                .map(transaction -> modelMapper.map(transaction, TransactionDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * Создает новую транзакцию и обновляет баланс счета.
     *
     * @param transactionDTO DTO с данными новой транзакции
     * @return DTO созданной транзакции
     * @throws EntityNotFoundException если брокерский счет не найден
     * @throws IllegalArgumentException если недостаточно средств для списания
     */
    @Transactional
    public TransactionDTO createTransaction(TransactionDTO transactionDTO) {
        BrokerAccount account = brokerAccountRepository.findByIdAndDeletedFalse(transactionDTO.getBrokerAccountId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Broker account not found with id: " + transactionDTO.getBrokerAccountId()));

        Transaction transaction = modelMapper.map(transactionDTO, Transaction.class);
        transaction.setBrokerAccount(account);

        if (transaction.getTransactionDate() == null) {
            transaction.setTransactionDate(LocalDateTime.now());
        }

        transaction.setDeleted(false);

        updateAccountBalance(account, transaction);

        brokerAccountRepository.save(account);
        Transaction savedTransaction = transactionRepository.save(transaction);
        return modelMapper.map(savedTransaction, TransactionDTO.class);
    }

    /**
     * Помечает транзакцию как удаленную (soft delete).
     *
     * @param id идентификатор транзакции для удаления
     * @throws EntityNotFoundException если транзакция не найдена
     */
    @Transactional
    public void deleteTransaction(Long id) {
        Transaction transaction = transactionRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new EntityNotFoundException("Transaction not found with id: " + id));
        transaction.setDeleted(true);
        transactionRepository.save(transaction);
    }

    /**
     * Обновляет баланс брокерского счета на основе транзакции.
     *
     * @param account брокерский счет
     * @param transaction транзакция
     * @throws IllegalArgumentException если недостаточно средств для списания
     */
    private void updateAccountBalance(BrokerAccount account, Transaction transaction) {
        String type = transaction.getType().toUpperCase();
        double amount = transaction.getAmount();

        switch (type) {
            case "DEPOSIT":
                account.setBalance(account.getBalance() + amount);
                break;
            case "WITHDRAWAL":
                if (account.getBalance() < amount) {
                    throw new IllegalArgumentException("Insufficient funds for withdrawal");
                }
                account.setBalance(account.getBalance() - amount);
                break;
            default:
                throw new IllegalArgumentException("Unknown transaction type: " + type);
        }
    }
}