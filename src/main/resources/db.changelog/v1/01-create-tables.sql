CREATE TABLE clients (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255),
    email VARCHAR(255) UNIQUE,
    phone_number VARCHAR(15)
);

CREATE TABLE brokerage_accounts (
    id SERIAL PRIMARY KEY,
    client_id INT REFERENCES clients(id),
    account_number VARCHAR(20) UNIQUE,
    balance DECIMAL(15, 2)
);

CREATE TABLE transactions (
    id SERIAL PRIMARY KEY,
    brokerage_account_id INT REFERENCES brokerage_accounts(id),
    transaction_date TIMESTAMP,
    amount DECIMAL(15, 2),
    type VARCHAR(50)
);