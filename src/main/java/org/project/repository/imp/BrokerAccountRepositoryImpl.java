package org.project.repository.imp;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.project.config.JDBCTemplateConfig;
import org.project.exception.EntityNotFoundException;
import org.project.model.BrokerAccount;
import org.project.repository.BrokerAccountRepository;
import org.project.repository.TransactionRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class BrokerAccountRepositoryImpl implements BrokerAccountRepository {

    private final JdbcTemplate jdbcTemplate = JDBCTemplateConfig.getJdbcTemplate();

    private final TransactionRepository transactionRepository;

    @Override
    public List<BrokerAccount> getBrokerAccounts() {
        String sql = "SELECT * FROM brokerage_accounts";
        return jdbcTemplate.query(sql, brokerAccountRowMapper);
    }

    @Override
    public BrokerAccount getBrokerAccount(Long id) {
        try {
            String sql = "SELECT * FROM brokerage_accounts WHERE id = ?";
            return jdbcTemplate.queryForObject(sql, brokerAccountRowMapper, id);
        } catch (Exception e) {
            throw new EntityNotFoundException("Broker account with id " + id + " not found");
        }

    }

    @Override
    public void createBrokerAccount(BrokerAccount brokerAccount) {
        String sql = "INSERT INTO brokerage_accounts (client_id, account_number, balance) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, brokerAccount.getClientId(), brokerAccount.getAccountNumber(), brokerAccount.getBalance());

    }

    @Override
    public void updateBrokerAccount(Long id, BrokerAccount brokerAccount) {
        String sql = "UPDATE brokerage_accounts SET client_id = ?, account_number = ?, balance = ? WHERE id = ?";
        int updateRow = jdbcTemplate.update(sql, brokerAccount.getClientId(), brokerAccount.getAccountNumber(), brokerAccount.getBalance(), id);
        if (updateRow == 0) {
            log.info("Broker account with id " + brokerAccount.getId() + " not found");
            createBrokerAccount(brokerAccount);
        }
    }

    @Override
    public void deleteBrokerAccount(Long id) {
        transactionRepository.deleteTransaction(id);

        String sql = "DELETE FROM brokerage_accounts WHERE id = ?";
        int deletedRow = jdbcTemplate.update(sql, id);
        if (deletedRow == 0) {
            throw new EntityNotFoundException("Broker account with id " + id + " not found");
        }
    }

    @Override
    public void deleteAll() {
        transactionRepository.deleteAllTransactions();
        String sql = "DELETE FROM brokerage_accounts";
        jdbcTemplate.update(sql);
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
