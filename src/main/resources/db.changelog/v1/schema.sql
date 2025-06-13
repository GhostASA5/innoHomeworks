-- Таблица клиентов
CREATE TABLE IF NOT EXISTS clients (
    id BIGINT PRIMARY KEY,
    name VARCHAR(255),
    email VARCHAR(255) UNIQUE,
    phone VARCHAR(15),
    password VARCHAR(15),
    role VARCHAR(15),
    deleted BOOLEAN
);

-- Таблица брокерских аккаунтов
CREATE TABLE IF NOT EXISTS brokerage_accounts (
    id BIGINT PRIMARY KEY,
    client_id BIGINT REFERENCES clients(id),
    account_number VARCHAR(20) UNIQUE,
    balance FLOAT,
    deleted BOOLEAN
);

-- Таблица транзакций
CREATE TABLE IF NOT EXISTS transactions (
    id BIGINT PRIMARY KEY,
    brokerage_account_id BIGINT REFERENCES brokerage_accounts(id),
    transaction_date TIMESTAMP,
    amount FLOAT,
    type VARCHAR(50),
    deleted BOOLEAN
);