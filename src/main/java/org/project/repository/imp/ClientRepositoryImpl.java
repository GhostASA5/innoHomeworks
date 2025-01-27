package org.project.repository.imp;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.project.config.JDBCTemplateConfig;
import org.project.exception.EntityNotFoundException;
import org.project.model.Client;
import org.project.repository.BrokerAccountRepository;
import org.project.repository.ClientRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class ClientRepositoryImpl implements ClientRepository {

    private final JdbcTemplate jdbcTemplate = JDBCTemplateConfig.getJdbcTemplate();

    private final BrokerAccountRepository brokerAccountRepository;

    @Override
    public List<Client> getClients() {
        String sql = "SELECT * FROM clients";
        return jdbcTemplate.query(sql, clientRowMapper);
    }

    @Override
    public Client getClient(Long id) {
        try {
            String sql = "SELECT * FROM clients WHERE id = ?";
            return jdbcTemplate.queryForObject(sql, clientRowMapper, id);
        } catch (EmptyResultDataAccessException ex) {
            throw new EntityNotFoundException("Client with id " + id + " not found");
        }

    }

    @Override
    public void addClient(Client client) {
        String sql = "INSERT INTO clients (name, email, phone_number) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, client.getName(), client.getEmail(), client.getPhone());
    }

    @Override
    public void updateClient(Long id, Client client) {
        String sql = "UPDATE clients SET name = ?, email = ?, phone_number = ? WHERE id = ?";
        int updatedRows = jdbcTemplate.update(sql, client.getName(), client.getEmail(), client.getPhone(), id);
        if(updatedRows == 0) {
            log.info("Client with id {} not exists", client.getId());
            addClient(client);
        }
    }

    @Override
    public void deleteClient(Long id) {
        brokerAccountRepository.deleteBrokerAccount(id);
        String sql = "DELETE FROM clients WHERE id = ?";
        int deletedRow = jdbcTemplate.update(sql, id);
        if (deletedRow == 0) {
            throw new EntityNotFoundException("Client with id " + id + " not found");
        }
    }

    @Override
    public void deleteAllClients() {
        brokerAccountRepository.deleteAll();
        String sql = "DELETE FROM clients";
        jdbcTemplate.update(sql);
    }

    public Client getByEmail(String email) {
        String sql = "SELECT * FROM clients WHERE email = ?";
        return jdbcTemplate.query(sql, clientRowMapper, email).get(0);
    }

    private static final RowMapper<Client> clientRowMapper = (rs, rowNum) -> {
        Client client = new Client();
        client.setId(rs.getLong("id"));
        client.setName(rs.getString("name"));
        client.setEmail(rs.getString("email"));
        client.setPhone(rs.getString("phone_number"));
        return client;
    };
}
