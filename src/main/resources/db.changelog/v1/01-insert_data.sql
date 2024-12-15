INSERT INTO clients (name, email, phone_number) VALUES
('John', 'john@example.com', '123456789'),
('Jane', 'jane@example.com', '987654321');

INSERT INTO brokerage_accounts (client_id, account_number, balance) VALUES
(1, 'ACC123456', 10000.00),
(2, 'ACC987654', 5000.00);

INSERT INTO transactions (brokerage_account_id, transaction_date, amount, type) VALUES
(1, '2024-12-15 10:00:00', 1500.00, 'Deposit'),
(2, '2024-12-15 11:00:00', 1000.00, 'Deposit');