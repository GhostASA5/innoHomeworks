package org.project.repository;

import org.project.model.Transaction;

import java.util.List;

public interface TransactionRepository {

    List<Transaction> getTransactions();

    Transaction getTransaction(Long id);

    int createTransaction(Transaction transaction);

}
