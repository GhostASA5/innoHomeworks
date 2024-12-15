package org.project.repository.imp;

import lombok.RequiredArgsConstructor;
import org.project.config.JDBCTemplateConfig;
import org.project.model.Client;
import org.project.repository.ClientRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;

@RequiredArgsConstructor
public class ClientRepositoryImpl implements ClientRepository {

    private final JdbcTemplate jdbcTemplate = JDBCTemplateConfig.getJdbcTemplate();

    @Override
    public List<Client> getClients() {
        String sql = "SELECT * FROM clients";
        return jdbcTemplate.query(sql, clientRowMapper);
    }

    @Override
    public Client getClient(Long id) {
        String sql = "SELECT * FROM clients WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, clientRowMapper, id);
    }

    @Override
    public void addClient(Client client) {
        String sql = "INSERT INTO clients (name, email, phone_number) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, client.getName(), client.getEmail(), client.getPhone());
    }

    @Override
    public void updateClient(Client client) {
        String sql = "UPDATE clients SET name = ?, email = ?, phone_number = ? WHERE id = ?";
        jdbcTemplate.update(sql, client.getName(), client.getEmail(), client.getPhone(), client.getId());
    }

    @Override
    public void deleteClient(Long id) {
        String sql = "DELETE FROM clients WHERE id = ?";
        jdbcTemplate.update(sql, id);
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
