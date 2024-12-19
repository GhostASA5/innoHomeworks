package org.project.repository.imp;

import org.project.config.JDBCTemplateConfig;
import org.project.model.Transaction;
import org.project.repository.TransactionRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;

public class TransactionRepositoryImpl implements TransactionRepository {

    private final JdbcTemplate jdbcTemplate = JDBCTemplateConfig.getJdbcTemplate();

    @Override
    public List<Transaction> getTransactions() {
        String sql = "SELECT * FROM transactions";
        return jdbcTemplate.query(sql, transactionRowMapper);
    }

    @Override
    public Transaction getTransaction(Long id) {
        String sql = "SELECT * FROM transactions WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, transactionRowMapper, id);
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
