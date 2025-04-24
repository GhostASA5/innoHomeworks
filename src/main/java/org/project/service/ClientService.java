package org.project.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.project.dto.ClientDTO;
import org.project.exception.EntityNotFoundException;
import org.project.model.Client;
import org.project.repository.ClientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;

    private final ModelMapper modelMapper;

    public List<ClientDTO> getAllClients() {
        return clientRepository.findAllByDeletedFalse().stream()
                .map(client -> modelMapper.map(client, ClientDTO.class))
                .collect(Collectors.toList());
    }

    public ClientDTO getClientById(Long id) {
        Client client = clientRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new EntityNotFoundException("Client not found with id: " + id));
        return modelMapper.map(client, ClientDTO.class);
    }

    @Transactional
    public ClientDTO createClient(ClientDTO clientDTO) {
        Client client = modelMapper.map(clientDTO, Client.class);
        client.setDeleted(false);
        Client savedClient = clientRepository.save(client);
        return modelMapper.map(savedClient, ClientDTO.class);
    }

    @Transactional
    public ClientDTO updateClient(Long id, ClientDTO clientDTO) {
        Client existingClient = clientRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new EntityNotFoundException("Client not found with id: " + id));

        modelMapper.map(clientDTO, existingClient);
        Client updatedClient = clientRepository.save(existingClient);
        return modelMapper.map(updatedClient, ClientDTO.class);
    }

    @Transactional
    public void deleteClient(Long id) {
        Client client = clientRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new EntityNotFoundException("Client not found with id: " + id));
        client.setDeleted(true);
        clientRepository.save(client);
    }
}
