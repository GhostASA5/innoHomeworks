package org.project.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.project.dto.ClientDTO;
import org.project.exception.EntityNotFoundException;
import org.project.model.Client;
import org.project.repository.ClientRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Сервис для работы с клиентами брокерского приложения.
 * Предоставляет CRUD операции для управления клиентами.
 */
@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final ModelMapper modelMapper;

    /**
     * Получает список всех активных клиентов.
     *
     * @return список DTO клиентов
     */
    @Cacheable(value = "clients", key = "'all'")
    public List<ClientDTO> getAllClients() {
        return clientRepository.findAllByDeletedFalse().stream()
                .map(client -> modelMapper.map(client, ClientDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * Получает клиента по идентификатору.
     *
     * @param id идентификатор клиента
     * @return DTO клиента
     * @throws EntityNotFoundException если клиент не найден
     */
    @Cacheable(value = "clients", key = "#id")
    public ClientDTO getClientById(Long id) {
        Client client = clientRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new EntityNotFoundException("Client not found with id: " + id));
        return modelMapper.map(client, ClientDTO.class);
    }

    /**
     * Создает нового клиента.
     *
     * @param clientDTO DTO с данными нового клиента
     * @return DTO созданного клиента
     */
    @Transactional
    @CachePut(value = "clients", key = "#result.id")
    public ClientDTO createClient(ClientDTO clientDTO) {
        Client client = modelMapper.map(clientDTO, Client.class);
        client.setDeleted(false);
        Client savedClient = clientRepository.save(client);
        return modelMapper.map(savedClient, ClientDTO.class);
    }

    /**
     * Обновляет данные клиента.
     *
     * @param id идентификатор клиента для обновления
     * @param clientDTO DTO с обновленными данными клиента
     * @return DTO обновленного клиента
     * @throws EntityNotFoundException если клиент не найден
     */
    @Transactional
    @CachePut(value = "clients", key = "#id")
    public ClientDTO updateClient(Long id, ClientDTO clientDTO) {
        Client existingClient = clientRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new EntityNotFoundException("Client not found with id: " + id));

        modelMapper.map(clientDTO, existingClient);
        Client updatedClient = clientRepository.save(existingClient);
        return modelMapper.map(updatedClient, ClientDTO.class);
    }

    /**
     * Помечает клиента как удаленного (soft delete).
     *
     * @param id идентификатор клиента для удаления
     * @throws EntityNotFoundException если клиент не найден
     */
    @Transactional
    @CacheEvict(value = "clients", key = "#id")
    public void deleteClient(Long id) {
        Client client = clientRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new EntityNotFoundException("Client not found with id: " + id));
        client.setDeleted(true);
        clientRepository.save(client);
    }
}
