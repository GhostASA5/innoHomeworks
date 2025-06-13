INSERT INTO clients (id, name, email, phone) VALUES
(1, 'John', 'john@example.com', '123456789'),
(2, 'Jane', 'jane@example.com', '987654321'),
(3, 'Alice', 'alice@example.com', '555123456'),
(4, 'Bob', 'bob@example.com', '444987654'),
(5, 'Charlie', 'charlie@example.com', '333567890'),
(6, 'Diana', 'diana@example.com', '222345678'),
(7, 'Edward', 'edward@example.com', '111654321'),
(8, 'Fiona', 'fiona@example.com', '666789012'),
(9, 'George', 'george@example.com', '777890123'),
(10, 'Hannah', 'hannah@example.com', '888901234');


INSERT INTO brokerage_accounts (id, client_id, account_number, balance, deleted) VALUES
(1, 1, 'ACC123456', 10000.00, false),
(2, 2, 'ACC987654', 5000.00, false),
(3, 3, 'ACC555123', 15000.00, false),
(4, 4, 'ACC444987', 7500.00, false),
(5, 5, 'ACC333567', 12000.00, false),
(6, 6, 'ACC222345', 20000.00, false),
(7, 7, 'ACC111654', 18000.00, false),
(8, 8, 'ACC666789', 9500.00, false),
(9, 9, 'ACC777890', 22000.00, false),
(10, 10, 'ACC888901', 12500.00, false);


INSERT INTO transactions (id, brokerage_account_id, transaction_date, amount, type) VALUES
(1, 1, '2024-12-15 10:00:00', 1500.00, 'Deposit'),
(2, 2, '2024-12-15 11:00:00', 1000.00, 'Deposit'),
(3, 3, '2024-12-16 09:30:00', 2000.00, 'Withdrawal'),
(4, 4, '2024-12-16 14:45:00', 500.00, 'Deposit'),
(5, 5, '2024-12-17 12:00:00', 2500.00, 'Deposit'),
(6, 6, '2024-12-17 15:30:00', 3000.00, 'Withdrawal'),
(7, 7, '2024-12-18 10:15:00', 1200.00, 'Deposit'),
(8, 8, '2024-12-18 13:00:00', 800.00, 'Withdrawal'),
(9, 9, '2024-12-19 11:45:00', 4000.00, 'Deposit'),
(10, 10, '2024-12-19 16:30:00', 1000.00, 'Withdrawal');
