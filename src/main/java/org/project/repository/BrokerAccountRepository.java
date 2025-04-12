package org.project.repository;

import org.project.model.BrokerAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BrokerAccountRepository extends JpaRepository<BrokerAccount, Long> {

    List<BrokerAccount> findAllByDeletedFalse();

    Optional<BrokerAccount> findByIdAndDeletedFalse(Long id);

    List<BrokerAccount> findAllByClientIdAndDeletedFalse(Long clientId);

    boolean existsByIdAndClientEmailAndDeletedFalse(Long accountId, String email);
}
