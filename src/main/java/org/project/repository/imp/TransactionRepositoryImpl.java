package org.project.repository.imp;

import lombok.extern.slf4j.Slf4j;
import org.project.config.JDBCTemplateConfig;
import org.project.exception.EntityNotFoundException;
import org.project.model.Transaction;
import org.project.repository.TransactionRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;

@Slf4j
public class TransactionRepositoryImpl implements TransactionRepository {

    private final JdbcTemplate jdbcTemplate = JDBCTemplateConfig.getJdbcTemplate();

    @Override
    public List<Transaction> getTransactions() {
        String sql = "SELECT * FROM transactions";
        return jdbcTemplate.query(sql, transactionRowMapper);
    }

    @Override
    public Transaction getTransaction(Long id) {
        try {
            String sql = "SELECT * FROM transactions WHERE id = ?";
            return jdbcTemplate.queryForObject(sql, transactionRowMapper, id);
        } catch (Exception e) {
            throw new EntityNotFoundException("Transaction with id " + id + " not found");
        }
    }

    @Override
    public int createTransaction(Transaction transaction) {
        String sql = "INSERT INTO transactions (brokerage_account_id, type, amount, transaction_date) VALUES (?, ?, ?, ?)";
        return jdbcTemplate.update(sql,
                transaction.getBrokerAccountId(),
                transaction.getType(),
                transaction.getAmount(),
                transaction.getTransactionDate());
    }

    @Override
    public void deleteTransaction(Long id) {
        String sql = "DELETE FROM transactions WHERE id = ?";
        int deletedRow = jdbcTemplate.update(sql, id);
        if (deletedRow == 0) {
            throw new EntityNotFoundException("Transaction with id " + id + " not found");
        }
    }

    @Override
    public void deleteAllTransactions() {
        String sql = "DELETE FROM transactions";
        jdbcTemplate.update(sql);
    }

    private static final RowMapper<Transaction> transactionRowMapper = (rs, rowNum) -> {
        Transaction transaction = new Transaction();
        transaction.setId(rs.getLong("id"));
        transaction.setBrokerAccountId(rs.getLong("brokerage_account_id"));
        transaction.setType(rs.getString("type"));
        transaction.setAmount(rs.getDouble("amount"));
        transaction.setTransactionDate(rs.getTimestamp("transaction_date").toLocalDateTime());
        return transaction;
    };
}
