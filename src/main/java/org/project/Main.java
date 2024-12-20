package org.project;

import org.project.repository.BrokerAccountRepository;
import org.project.repository.ClientRepository;
import org.project.repository.TransactionRepository;
import org.project.repository.imp.BrokerAccountRepositoryImpl;
import org.project.repository.imp.ClientRepositoryImpl;
import org.project.repository.imp.TransactionRepositoryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {

    private static final ClientRepository clientRepository = new ClientRepositoryImpl();
    private static final BrokerAccountRepository accountRepository = new BrokerAccountRepositoryImpl();
    private static final TransactionRepository transactionRepository = new TransactionRepositoryImpl();

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
        clientRepository.getClients().forEach(System.out::println);
        accountRepository.getBrokerAccounts().forEach(System.out::println);
        transactionRepository.getTransactions().forEach(System.out::println);

        System.out.println(clientRepository.getClient(1L));
        System.out.println(accountRepository.getBrokerAccount(1L));
        System.out.println(transactionRepository.getTransaction(1L));
    }
}