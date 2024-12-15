package org.project.repository.imp;

import org.project.config.JDBCTemplateConfig;
import org.project.model.BrokerAccount;
import org.project.repository.BrokerAccountRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;

public class BrokerAccountRepositoryImpl implements BrokerAccountRepository {

    private final JdbcTemplate jdbcTemplate = JDBCTemplateConfig.getJdbcTemplate();

    @Override
    public List<BrokerAccount> getBrokerAccounts() {
        String sql = "SELECT * FROM brokerage_accounts";
        return jdbcTemplate.query(sql, brokerAccountRowMapper);
    }

    @Override
    public BrokerAccount getBrokerAccount(Long id) {
        String sql = "SELECT * FROM brokerage_accounts WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, brokerAccountRowMapper, id);
    }

    @Override
    public void createBrokerAccount(BrokerAccount brokerAccount) {
        String sql = "INSERT INTO brokerage_accounts (client_id, account_number, balance) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, brokerAccount.getClientId(), brokerAccount.getAccountNumber(), brokerAccount.getBalance());

    }

    @Override
    public void updateBrokerAccount(BrokerAccount brokerAccount) {
        String sql = "UPDATE brokerage_accounts SET client_id = ?, account_number = ?, balance = ? WHERE id = ?";
        jdbcTemplate.update(sql, brokerAccount.getClientId(), brokerAccount.getAccountNumber(), brokerAccount.getBalance(), brokerAccount.getId());
    }

    @Override
    public void deleteBrokerAccount(Long id) {
        String sql = "DELETE FROM brokerage_accounts WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    private static final RowMapper<BrokerAccount> brokerAccountRowMapper = (rs, rowNum) -> {
        BrokerAccount account = new BrokerAccount();
        account.setId(rs.getLong("id"));
        account.setClientId(rs.getLong("client_id"));
        account.setAccountNumber(rs.getString("account_number"));
        account.setBalance(rs.getDouble("balance"));
        return account;
    };
}
