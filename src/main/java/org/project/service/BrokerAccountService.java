package org.project.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.project.dto.BrokerAccountDTO;
import org.project.exception.EntityNotFoundException;
import org.project.model.BrokerAccount;
import org.project.model.Client;
import org.project.repository.BrokerAccountRepository;
import org.project.repository.ClientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BrokerAccountService {

    private final BrokerAccountRepository brokerAccountRepository;
    private final ClientRepository clientRepository;
    private final ModelMapper modelMapper;

    public List<BrokerAccountDTO> getAllBrokerAccounts() {
        return brokerAccountRepository.findAllByDeletedFalse().stream()
                .map(account -> modelMapper.map(account, BrokerAccountDTO.class))
                .collect(Collectors.toList());
    }

    public BrokerAccountDTO getBrokerAccountById(Long id) {
        BrokerAccount account = brokerAccountRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new EntityNotFoundException("Broker account not found with id: " + id));
        return modelMapper.map(account, BrokerAccountDTO.class);
    }

    public List<BrokerAccountDTO> getBrokerAccountsByClientId(Long clientId) {
        return brokerAccountRepository.findAllByClientIdAndDeletedFalse(clientId).stream()
                .map(account -> modelMapper.map(account, BrokerAccountDTO.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public BrokerAccountDTO createBrokerAccount(BrokerAccountDTO brokerAccountDTO) {
        Client client = clientRepository.findByIdAndDeletedFalse(brokerAccountDTO.getClientId())
                .orElseThrow(() -> new EntityNotFoundException("Client not found with id: " + brokerAccountDTO.getClientId()));

        BrokerAccount account = modelMapper.map(brokerAccountDTO, BrokerAccount.class);
        account.setClient(client);
        account.setDeleted(false);

        BrokerAccount savedAccount = brokerAccountRepository.save(account);
        return modelMapper.map(savedAccount, BrokerAccountDTO.class);
    }

    @Transactional
    public BrokerAccountDTO updateBrokerAccount(Long id, BrokerAccountDTO brokerAccountDTO) {
        BrokerAccount existingAccount = brokerAccountRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new EntityNotFoundException("Broker account not found with id: " + id));

        Client client = clientRepository.findByIdAndDeletedFalse(brokerAccountDTO.getClientId())
                .orElseThrow(() -> new EntityNotFoundException("Client not found with id: " + brokerAccountDTO.getClientId()));

        modelMapper.map(brokerAccountDTO, existingAccount);
        existingAccount.setClient(client);

        BrokerAccount updatedAccount = brokerAccountRepository.save(existingAccount);
        return modelMapper.map(updatedAccount, BrokerAccountDTO.class);
    }

    @Transactional
    public void deleteBrokerAccount(Long id) {
        BrokerAccount account = brokerAccountRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new EntityNotFoundException("Broker account not found with id: " + id));
        account.setDeleted(true);
        brokerAccountRepository.save(account);
    }
}
