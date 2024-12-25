-- Таблица клиентов
CREATE TABLE IF NOT EXISTS clients (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255),
    email VARCHAR(255) UNIQUE,
    phone_number VARCHAR(15)
);

-- Таблица брокерских аккаунтов
CREATE TABLE IF NOT EXISTS brokerage_accounts (
    id SERIAL PRIMARY KEY,
    client_id INT REFERENCES clients(id),
    account_number VARCHAR(20) UNIQUE,
    balance DECIMAL(15, 2)
);

-- Таблица транзакций
CREATE TABLE IF NOT EXISTS transactions (
    id SERIAL PRIMARY KEY,
    brokerage_account_id INT REFERENCES brokerage_accounts(id),
    transaction_date TIMESTAMP,
    amount DECIMAL(15, 2),
    type VARCHAR(50)
);