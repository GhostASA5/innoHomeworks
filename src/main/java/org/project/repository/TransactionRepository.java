package org.project.repository;

import org.project.model.Transaction;

import java.util.List;

public interface TransactionRepository {

    List<Transaction> getTransactions();

    Transaction getTransaction(Long id);

    void createTransaction(Transaction transaction);

    void deleteTransaction(Long id);

    void deleteAllTransactions();

}
