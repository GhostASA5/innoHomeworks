package org.project.repository;

import org.project.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    Optional<Client> findByEmail(String email);

    List<Client> findAllByDeletedFalse();

    Optional<Client> findByIdAndDeletedFalse(Long id);

    boolean existsByIdAndEmailAndDeletedFalse(Long clientId, String email);
}
