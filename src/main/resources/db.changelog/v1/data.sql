INSERT INTO clients (name, email, phone_number) VALUES
('John', 'john@example.com', '123456789'),
('Jane', 'jane@example.com', '987654321'),
('Alice', 'alice@example.com', '555123456'),
('Bob', 'bob@example.com', '444987654'),
('Charlie', 'charlie@example.com', '333567890'),
('Diana', 'diana@example.com', '222345678'),
('Edward', 'edward@example.com', '111654321'),
('Fiona', 'fiona@example.com', '666789012'),
('George', 'george@example.com', '777890123'),
('Hannah', 'hannah@example.com', '888901234');


INSERT INTO brokerage_accounts (client_id, account_number, balance) VALUES
(1, 'ACC123456', 10000.00),
(2, 'ACC987654', 5000.00),
(3, 'ACC555123', 15000.00),
(4, 'ACC444987', 7500.00),
(5, 'ACC333567', 12000.00),
(6, 'ACC222345', 20000.00),
(7, 'ACC111654', 18000.00),
(8, 'ACC666789', 9500.00),
(9, 'ACC777890', 22000.00),
(10, 'ACC888901', 12500.00);


INSERT INTO transactions (brokerage_account_id, transaction_date, amount, type) VALUES
(1, '2024-12-15 10:00:00', 1500.00, 'Deposit'),
(2, '2024-12-15 11:00:00', 1000.00, 'Deposit'),
(3, '2024-12-16 09:30:00', 2000.00, 'Withdrawal'),
(4, '2024-12-16 14:45:00', 500.00, 'Deposit'),
(5, '2024-12-17 12:00:00', 2500.00, 'Deposit'),
(6, '2024-12-17 15:30:00', 3000.00, 'Withdrawal'),
(7, '2024-12-18 10:15:00', 1200.00, 'Deposit'),
(8, '2024-12-18 13:00:00', 800.00, 'Withdrawal'),
(9, '2024-12-19 11:45:00', 4000.00, 'Deposit'),
(10, '2024-12-19 16:30:00', 1000.00, 'Withdrawal');
