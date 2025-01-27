package org.project.repository;

import org.project.model.Client;

import java.util.List;


public interface ClientRepository {

    List<Client> getClients();

    Client getClient(Long id);

    void addClient(Client client);

    void updateClient(Long id, Client client);

    void deleteClient(Long id);

    void deleteAllClients();

    Client getByEmail(String email);
}
