package org.project.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.project.dto.TransactionDTO;
import org.project.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<TransactionDTO>> getAllTransactions() {
        return ResponseEntity.ok(transactionService.getAllTransactions());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @clientSecurityService.isTransactionOwner(#id, authentication)")
    public ResponseEntity<TransactionDTO> getTransactionById(@PathVariable Long id) {
        return ResponseEntity.ok(transactionService.getTransactionById(id));
    }

    @GetMapping("/account/{brokerAccountId}")
    @PreAuthorize("hasRole('ADMIN') or @clientSecurityService.isAccountOwner(#brokerAccountId, authentication)")
    public ResponseEntity<List<TransactionDTO>> getTransactionsByBrokerAccountId(
            @PathVariable Long brokerAccountId) {
        return ResponseEntity.ok(transactionService.getTransactionsByBrokerAccountId(brokerAccountId));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or @clientSecurityService.isAccountOwner(#transactionDTO.brokerAccountId, authentication)")
    public ResponseEntity<TransactionDTO> createTransaction(
            @Valid @RequestBody TransactionDTO transactionDTO) {
        return new ResponseEntity<>(transactionService.createTransaction(transactionDTO), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @clientSecurityService.isTransactionOwner(#id, authentication)")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        transactionService.deleteTransaction(id);
        return ResponseEntity.noContent().build();
    }
}