package org.project.repository;

import org.project.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findAllByDeletedFalse();

    Optional<Transaction> findByIdAndDeletedFalse(Long id);

    List<Transaction> findAllByBrokerAccountIdAndDeletedFalse(Long brokerAccountId);

    boolean existsByIdAndBrokerAccountClientEmailAndDeletedFalse(Long transactionId, String email);
}
