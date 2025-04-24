package org.project.service;

import lombok.RequiredArgsConstructor;
import org.project.repository.BrokerAccountRepository;
import org.project.repository.ClientRepository;
import org.project.repository.TransactionRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientSecurityService {

    private final ClientRepository clientRepository;
    private final BrokerAccountRepository brokerAccountRepository;
    private final TransactionRepository transactionRepository;

    public boolean isClientOwner(Long clientId, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();

        return clientRepository.existsByIdAndEmailAndDeletedFalse(clientId, email);
    }

    public boolean isAccountOwner(Long accountId, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();

        return brokerAccountRepository.existsByIdAndClientEmailAndDeletedFalse(accountId, email);
    }

    public boolean isTransactionOwner(Long transactionId, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();

        return transactionRepository.existsByIdAndBrokerAccountClientEmailAndDeletedFalse(transactionId, email);
    }
}
