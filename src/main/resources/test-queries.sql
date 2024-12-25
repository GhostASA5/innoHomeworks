-- Получить все записи
SELECT * FROM clients;

-- Получить записи с определенным email
SELECT * FROM clients WHERE email = 'john@example.com';

-- Получить записи с определенным именем
SELECT * FROM clients WHERE name LIKE 'J%';

-- Обновить номер телефона клиента
UPDATE clients
SET phone_number = '111222333'
WHERE id = 1;

-- Обновить email всех клиентов с именем "Jane"
UPDATE clients
SET email = 'jane.doe@example.com'
WHERE name = 'Jane';

-- Удалить клиента по ID
DELETE FROM clients
WHERE id = 1;

-- Удалить всех клиентов с определенным email
DELETE FROM clients
WHERE email = 'jane@example.com';

-- Получить все записи
SELECT * FROM brokerage_accounts;

-- Получить записи с балансом больше 10000
SELECT * FROM brokerage_accounts WHERE balance > 10000;

-- Получить счета конкретного клиента
SELECT * FROM brokerage_accounts WHERE client_id = 2;

-- Обновить баланс на счету
UPDATE brokerage_accounts
SET balance = balance + 500.00
WHERE account_number = 'ACC123456';

-- Изменить номер счета для клиента
UPDATE brokerage_accounts
SET account_number = 'ACC654321'
WHERE client_id = 2;