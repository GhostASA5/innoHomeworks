package org.project.repository;

import org.project.model.BrokerAccount;

import java.util.List;

public interface BrokerAccountRepository {

    List<BrokerAccount> getBrokerAccounts();

    BrokerAccount getBrokerAccount(Long id);

    void createBrokerAccount(BrokerAccount brokerAccount);

    void updateBrokerAccount(Long id, BrokerAccount brokerAccount);

    void deleteBrokerAccount(Long id);

    void deleteAll();
}
