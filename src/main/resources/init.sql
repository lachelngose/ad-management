-- src/main/resources/init.sql

-- Insert campaigns
INSERT INTO campaign (name, customer_id, start_date, end_date, budget, targets, is_active, payment_status, created_by)
VALUES ('Campaign 1', 1, '2024-01-01 00:00:00', '2024-12-31 23:59:59', 1000.00, '{"region":["North America","Europe"],"gender":["MALE","FEMALE"],"age":["TEEN","ADULT"]}', true, 'PAID', 1),
       ('Campaign 2', 1, '2024-01-01 00:00:00', '2024-12-31 23:59:59', 2000.00, '{"region":["Asia","Australia"],"gender":["FEMALE"],"age":["YOUNG_ADULT","SENIOR"]}', true, 'PAID', 1),
       ('Campaign 3', 1, '2024-01-01 00:00:00', '2024-12-31 23:59:59', 3000.00, '{"region":["South America"],"gender":["MALE"],"age":["ADULT"]}', true, 'PAID', 1),
       ('Campaign 4', 1, '2024-01-01 00:00:00', '2024-12-31 23:59:59', 4000.00, '{"region":["Africa","Europe"],"gender":["FEMALE"],"age":["TEEN","YOUNG_ADULT"]}', true, 'PAID', 1),
       ('Campaign 5', 1, '2024-01-01 00:00:00', '2024-12-31 23:59:59', 5000.00, '{"region":["North America","Asia"],"gender":["MALE","FEMALE"],"age":["SENIOR"]}', true, 'PAID', 1),
       ('Campaign 6', 2, '2024-01-01 00:00:00', '2024-12-31 23:59:59', 6000.00, '{"region":["Australia"],"gender":["MALE"],"age":["TEEN","ADULT","SENIOR"]}', true, 'PAID', 1),
       ('Campaign 7', 2, '2024-01-01 00:00:00', '2024-12-31 23:59:59', 7000.00, '{"region":["Europe"],"gender":["FEMALE"],"age":["YOUNG_ADULT"]}', true, 'PAID', 1),
       ('Campaign 8', 2, '2024-01-01 00:00:00', '2024-12-31 23:59:59', 8000.00, '{"region":["North America","South America"],"gender":["MALE","FEMALE"],"age":["TEEN","YOUNG_ADULT","ADULT"]}', true, 'PAID', 1),
       ('Campaign 9', 2, '2024-01-01 00:00:00', '2024-12-31 23:59:59', 9000.00, '{"region":["Asia","Africa"],"gender":["MALE"],"age":["ADULT","SENIOR"]}', true, 'PAID', 1),
       ('Campaign 10', 2, '2024-01-01 00:00:00', '2024-12-31 23:59:59', 10000.00, '{"region":["Australia","Europe"],"gender":["FEMALE"],"age":["TEEN","ADULT"]}', true, 'PAID', 1);

-- Insert customers
INSERT INTO customer (name)
VALUES ('Customer 1'),
       ('Customer 2');

-- Insert paymentInfo
INSERT INTO payment_info (customer_id, payment_method, payment_customer_id)
VALUES (1, 'CREDIT_CARD', 'payment-customer-1'),
       (1, 'BANK_TRANSFER', 'payment-customer-1'),
       (2, 'CREDIT_CARD', 'payment-customer-2'),
       (2, 'BANK_TRANSFER', 'payment-customer-2');

-- Insert paidHistory
INSERT INTO paid_history (customer_id, campaign_id, amount, payment_info_id, transaction_id, status)
VALUES (1, 1, 1000.00, 1, 'transaction-1', 'PAID'),
       (1, 1, 2000.00, 1, 'transaction-2', 'PAID'),
       (1, 2, 3000.00, 1, 'transaction-3', 'PAID'),
       (1, 2, 4000.00, 1, 'transaction-4', 'PAID'),
       (1, 3, 5000.00, 1, 'transaction-5', 'PAID'),
       (1, 3, 6000.00, 2, 'transaction-6', 'PAID'),
       (1, 4, 7000.00, 2, 'transaction-7', 'PAID'),
       (1, 4, 8000.00, 2, 'transaction-8', 'PAID'),
       (1, 5, 9000.00, 2, 'transaction-9', 'PAID'),
       (1, 5, 10000.00, 2, 'transaction-10', 'PAID'),
       (2, 6, 1000.00, 3, 'transaction-11', 'PAID'),
       (2, 6, 2000.00, 3, 'transaction-12', 'PAID'),
       (2, 7, 3000.00, 3, 'transaction-13', 'PAID'),
       (2, 7, 4000.00, 3, 'transaction-14', 'PAID'),
       (2, 8, 5000.00, 3, 'transaction-15', 'PAID'),
       (2, 8, 6000.00, 4, 'transaction-16', 'PAID'),
       (2, 9, 7000.00, 4, 'transaction-17', 'PAID'),
       (2, 9, 8000.00, 4, 'transaction-18', 'PAID'),
       (2, 10, 9000.00, 4, 'transaction-19', 'PAID'),
       (2, 10, 10000.00, 4, 'transaction-20', 'PAID');