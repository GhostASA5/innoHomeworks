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

/**
 * Сервис для работы с брокерскими счетами.
 * Предоставляет CRUD операции для управления брокерскими счетами клиентов.
 */
@Service
@RequiredArgsConstructor
public class BrokerAccountService {

    private final BrokerAccountRepository brokerAccountRepository;
    private final ClientRepository clientRepository;
    private final ModelMapper modelMapper;

    /**
     * Получает список всех активных брокерских счетов.
     *
     * @return список DTO брокерских счетов
     */
    public List<BrokerAccountDTO> getAllBrokerAccounts() {
        return brokerAccountRepository.findAllByDeletedFalse().stream()
                .map(account -> modelMapper.map(account, BrokerAccountDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * Получает брокерский счет по идентификатору.
     *
     * @param id идентификатор счета
     * @return DTO брокерского счета
     * @throws EntityNotFoundException если счет не найден
     */
    public BrokerAccountDTO getBrokerAccountById(Long id) {
        BrokerAccount account = brokerAccountRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new EntityNotFoundException("Broker account not found with id: " + id));
        return modelMapper.map(account, BrokerAccountDTO.class);
    }

    /**
     * Получает список брокерских счетов по идентификатору клиента.
     *
     * @param clientId идентификатор клиента
     * @return список DTO брокерских счетов
     */
    public List<BrokerAccountDTO> getBrokerAccountsByClientId(Long clientId) {
        return brokerAccountRepository.findAllByClientIdAndDeletedFalse(clientId).stream()
                .map(account -> modelMapper.map(account, BrokerAccountDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * Создает новый брокерский счет.
     *
     * @param brokerAccountDTO DTO с данными нового счета
     * @return DTO созданного счета
     * @throws EntityNotFoundException если клиент не найден
     */
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

    /**
     * Обновляет данные брокерского счета.
     *
     * @param id идентификатор счета для обновления
     * @param brokerAccountDTO DTO с обновленными данными счета
     * @return DTO обновленного счета
     * @throws EntityNotFoundException если счет или клиент не найдены
     */
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

    /**
     * Помечает брокерский счет как удаленный (soft delete).
     *
     * @param id идентификатор счета для удаления
     * @throws EntityNotFoundException если счет не найден
     */
    @Transactional
    public void deleteBrokerAccount(Long id) {
        BrokerAccount account = brokerAccountRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new EntityNotFoundException("Broker account not found with id: " + id));
        account.setDeleted(true);
        brokerAccountRepository.save(account);
    }
}