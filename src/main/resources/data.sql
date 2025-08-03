-- Sample data for testing the banking system
-- Note: Passwords are BCrypt encoded versions of 'password123'

-- Insert sample users
INSERT IGNORE INTO users (id, username, password, email, balance, created_at, updated_at) VALUES
(1, 'john_doe', '$2a$10$VEjxXn.6zQp8E8pEZrKexOn6CjvQzBYJTaZI9r.MkOKw9l.GQjX/a', 'john@example.com', 1000.00, NOW(), NOW()),
(2, 'jane_smith', '$2a$10$VEjxXn.6zQp8E8pEZrKexOn6CjvQzBYJTaZI9r.MkOKw9l.GQjX/a', 'jane@example.com', 1500.00, NOW(), NOW()),
(3, 'bob_wilson', '$2a$10$VEjxXn.6zQp8E8pEZrKexOn6CjvQzBYJTaZI9r.MkOKw9l.GQjX/a', 'bob@example.com', 750.00, NOW(), NOW());

-- Insert sample transactions
INSERT IGNORE INTO transactions (id, sender_id, receiver_id, amount, timestamp, type, description) VALUES
(1, NULL, 1, 500.00, DATE_SUB(NOW(), INTERVAL 10 DAY), 'CREDIT', 'Initial deposit'),
(2, NULL, 2, 1000.00, DATE_SUB(NOW(), INTERVAL 9 DAY), 'CREDIT', 'Initial deposit'),
(3, NULL, 3, 300.00, DATE_SUB(NOW(), INTERVAL 8 DAY), 'CREDIT', 'Initial deposit'),
(4, 1, 2, 100.00, DATE_SUB(NOW(), INTERVAL 7 DAY), 'TRANSFER', 'Transfer from john_doe to jane_smith'),
(5, 2, 3, 200.00, DATE_SUB(NOW(), INTERVAL 6 DAY), 'TRANSFER', 'Transfer from jane_smith to bob_wilson'),
(6, NULL, 1, 500.00, DATE_SUB(NOW(), INTERVAL 5 DAY), 'CREDIT', 'Salary deposit'),
(7, 3, NULL, 50.00, DATE_SUB(NOW(), INTERVAL 4 DAY), 'DEBIT', 'ATM withdrawal'),
(8, 1, 3, 150.00, DATE_SUB(NOW(), INTERVAL 3 DAY), 'TRANSFER', 'Payment to bob_wilson'),
(9, NULL, 2, 500.00, DATE_SUB(NOW(), INTERVAL 2 DAY), 'CREDIT', 'Bonus payment'),
(10, 2, NULL, 100.00, DATE_SUB(NOW(), INTERVAL 1 DAY), 'DEBIT', 'Online purchase');