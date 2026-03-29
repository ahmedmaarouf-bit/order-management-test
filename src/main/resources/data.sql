-- Insert Customers
INSERT INTO customers (id, name, email, phone, created_at) VALUES 
(1, 'John Doe', 'john.doe@email.com', '555-1234', TIMESTAMP '2024-01-15 10:30:00'),
(2, 'Jane Smith', 'jane.smith@email.com', '555-5678', TIMESTAMP '2024-01-16 14:20:00'),
(3, 'Bob Johnson', 'bob.johnson@email.com', '555-9012', TIMESTAMP '2024-01-17 09:15:00'),
(4, 'Alice Williams', 'alice.williams@email.com', '555-3456', TIMESTAMP '2024-01-18 16:45:00'),
(5, 'Charlie Brown', 'charlie.brown@email.com', '555-7890', TIMESTAMP '2024-01-19 11:00:00');

-- Insert Products
INSERT INTO products (id, name, price, stock, category) VALUES 
(1, 'Laptop', 999.99, 50, 'Electronics'),
(2, 'Wireless Mouse', 19.99, 200, 'Electronics'),
(3, 'USB Cable', 14.99, 500, 'Accessories'),
(4, 'Monitor', 299.99, 30, 'Electronics'),
(5, 'Keyboard', 79.99, 150, 'Electronics'),
(6, 'Headphones', 149.99, 80, 'Audio'),
(7, 'Webcam', 89.99, 60, 'Electronics'),
(8, 'Mouse Pad', 9.99, 300, 'Accessories'),
(9, 'USB Hub', 24.99, 100, 'Accessories'),
(10, 'Desk Lamp', 34.99, 75, 'Office');

-- Insert Orders
INSERT INTO orders (id, customer_id, order_date, status, total_amount) VALUES 
(1, 1, '2024-01-20', 'DELIVERED', 1019.98),
(2, 2, '2024-01-21', 'SHIPPED', 179.98),
(3, 3, '2024-01-22', 'CONFIRMED', 44.98),
(4, 1, '2024-01-23', 'PENDING', 149.99),
(5, 4, '2024-01-24', 'DELIVERED', 379.98),
(6, 5, '2024-01-25', 'SHIPPED', 1099.98),
(7, 2, '2024-01-26', 'CONFIRMED', 94.98),
(8, 3, '2024-01-27', 'PENDING', 239.98),
(9, 1, '2024-01-28', 'DELIVERED', 29.98),
(10, 4, '2024-01-29', 'SHIPPED', 89.99),
(11, 5, '2024-01-30', 'CONFIRMED', 59.98),
(12, 2, '2024-01-31', 'PENDING', 329.98),
(13, 3, '2024-02-01', 'DELIVERED', 124.98),
(14, 1, '2024-02-02', 'SHIPPED', 429.98),
(15, 4, '2024-02-03', 'CONFIRMED', 19.98);

-- Insert Order Items
INSERT INTO order_items (id, order_id, product_id, quantity, unit_price) VALUES 
-- Order 1: John Doe - Laptop + Wireless Mouse
(1, 1, 1, 1, 999.99),
(2, 1, 2, 1, 19.99),
-- Order 2: Jane Smith - Keyboard + Mouse
(3, 2, 5, 1, 79.99),
(4, 2, 2, 5, 19.99),
-- Order 3: Bob Johnson - USB Cable + Mouse Pad
(5, 3, 3, 2, 14.99),
(6, 3, 8, 1, 9.99),
-- Order 4: John Doe - Headphones
(7, 4, 6, 1, 149.99),
-- Order 5: Alice Williams - Monitor + Keyboard
(8, 5, 4, 1, 299.99),
(9, 5, 5, 1, 79.99),
-- Order 6: Charlie Brown - Laptop + Webcam
(10, 6, 1, 1, 999.99),
(11, 6, 7, 1, 89.99),
-- Order 7: Jane Smith - USB Hub + USB Cable
(12, 7, 9, 1, 24.99),
(13, 7, 3, 4, 14.99),
-- Order 8: Bob Johnson - Headphones + Webcam
(14, 8, 6, 1, 149.99),
(15, 8, 7, 1, 89.99),
-- Order 9: John Doe - Mouse Pad + USB Cable
(16, 9, 8, 1, 9.99),
(17, 9, 3, 1, 14.99),
-- Order 10: Alice Williams - Webcam
(18, 10, 7, 1, 89.99),
-- Order 11: Charlie Brown - Mouse Pad + USB Hub
(19, 11, 8, 2, 9.99),
(20, 11, 9, 1, 24.99),
-- Order 12: Jane Smith - Monitor + USB Hub
(21, 12, 4, 1, 299.99),
(22, 12, 9, 1, 24.99),
-- Order 13: Bob Johnson - Keyboard + Mouse Pad + Desk Lamp
(23, 13, 5, 1, 79.99),
(24, 13, 8, 1, 9.99),
(25, 13, 10, 1, 34.99),
-- Order 14: John Doe - Monitor + Mouse
(26, 14, 4, 1, 299.99),
(27, 14, 2, 6, 19.99),
-- Order 15: Alice Williams - Mouse + USB Cable
(28, 15, 2, 1, 19.99),
(29, 15, 3, 1, 14.99);