package org.project.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.project.model.Transaction;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionSecurityService {

    public void secureTransaction(Transaction transaction) {
        log.info("Find transaction with big amount. Transaction id - {}, amount - {}", transaction.getId(), transaction.getAmount());
        // какая-то логика
    }
}
