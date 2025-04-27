package org.project.service;

import lombok.RequiredArgsConstructor;
import org.project.repository.BrokerAccountRepository;
import org.project.repository.ClientRepository;
import org.project.repository.TransactionRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 * Сервис для проверки прав доступа к ресурсам приложения.
 * Обеспечивает проверку принадлежности ресурсов (клиентов, счетов, транзакций) текущему пользователю.
 */
@Service
@RequiredArgsConstructor
public class ClientSecurityService {

    private final ClientRepository clientRepository;
    private final BrokerAccountRepository brokerAccountRepository;
    private final TransactionRepository transactionRepository;

    /**
     * Проверяет, является ли текущий аутентифицированный пользователь владельцем клиентского аккаунта.
     *
     * @param clientId идентификатор клиента для проверки
     * @param authentication объект аутентификации текущего пользователя
     * @return true если пользователь является владельцем аккаунта, иначе false
     */
    public boolean isClientOwner(Long clientId, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();

        return clientRepository.existsByIdAndEmailAndDeletedFalse(clientId, email);
    }

    /**
     * Проверяет, является ли текущий аутентифицированный пользователь владельцем брокерского счета.
     *
     * @param accountId идентификатор брокерского счета для проверки
     * @param authentication объект аутентификации текущего пользователя
     * @return true если пользователь является владельцем счета, иначе false
     */
    public boolean isAccountOwner(Long accountId, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();

        return brokerAccountRepository.existsByIdAndClientEmailAndDeletedFalse(accountId, email);
    }

    /**
     * Проверяет, является ли текущий аутентифицированный пользователь владельцем транзакции.
     *
     * @param transactionId идентификатор транзакции для проверки
     * @param authentication объект аутентификации текущего пользователя
     * @return true если пользователь является владельцем транзакции, иначе false
     */
    public boolean isTransactionOwner(Long transactionId, Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();

        return transactionRepository.existsByIdAndBrokerAccountClientEmailAndDeletedFalse(transactionId, email);
    }
}