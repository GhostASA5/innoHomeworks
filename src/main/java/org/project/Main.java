package org.project;

import org.project.model.BrokerAccount;
import org.project.model.Client;
import org.project.model.Transaction;
import org.project.repository.BrokerAccountRepository;
import org.project.repository.ClientRepository;
import org.project.repository.TransactionRepository;
import org.project.repository.imp.BrokerAccountRepositoryImpl;
import org.project.repository.imp.ClientRepositoryImpl;
import org.project.repository.imp.TransactionRepositoryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;

@SpringBootApplication
public class Main {


    private static final TransactionRepository transactionRepository = new TransactionRepositoryImpl();
    private static final BrokerAccountRepository accountRepository = new BrokerAccountRepositoryImpl(transactionRepository);
    private static final ClientRepository clientRepository = new ClientRepositoryImpl(accountRepository);

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
        clientRepository.getClients().forEach(System.out::println);

        System.out.println(clientRepository.getClient(1L));
        System.out.println(clientRepository.getClients());
        System.out.println(clientRepository.getClient(1L));

        clientRepository.addClient(Client.builder().email("ex@mail.ru").build());
        clientRepository.updateClient(1L, Client.builder().name("John New").build());
        System.out.println(clientRepository.getClient(1L));
        System.out.println(clientRepository.getByEmail("jane@example.com"));


        accountRepository.getBrokerAccounts().forEach(System.out::println);
        System.out.println(accountRepository.getBrokerAccount(1L));
        System.out.println(accountRepository.getBrokerAccounts().size());
        accountRepository.createBrokerAccount(BrokerAccount.builder().accountNumber("ADD111654").build());
        accountRepository.updateBrokerAccount(11L, BrokerAccount.builder().accountNumber("ADD111654").balance(10000.0).build());
        System.out.println(accountRepository.getBrokerAccounts().size());


        transactionRepository.getTransactions().forEach(System.out::println);
        System.out.println(transactionRepository.getTransaction(1L));
        transactionRepository.createTransaction(Transaction.builder().brokerAccountId(11L).amount(500.0).transactionDate(LocalDateTime.now()).build());
        System.out.println(transactionRepository.getTransaction(10L));

        clientRepository.deleteClient(1L);

        transactionRepository.deleteAllTransactions();
        accountRepository.deleteAll();
        clientRepository.deleteAllClients();

        System.out.println(clientRepository.getClients().size());
        System.out.println(accountRepository.getBrokerAccounts().size());
        System.out.println(transactionRepository.getTransactions().size());
    }
}