DELETE
FROM Productos;


ALTER TABLE Productos
    ALTER COLUMN id RESTART WITH 1;

INSERT INTO Productos (nombre, precio, stock, categoria, created_at, updated_at, deleted)
VALUES ('Laptop HP Pavilion', 899.99, 15, 'ELECTRONICA', '2025-03-18 10:00:00', '2025-03-18 10:00:00', FALSE),
       ('iPhone 14 Pro', 1199.99, 25, 'ELECTRONICA', '2025-03-18 10:01:00', '2025-03-18 10:01:00', FALSE),
       ('Zapatillas Nike Air Max', 129.99, 30, 'DEPORTE', '2025-03-18 10:02:00', '2025-03-18 10:02:00', FALSE),
       ('Smart TV Samsung 55"', 699.99, 20, 'ELECTRONICA', '2025-03-18 10:03:00', '2025-03-18 10:03:00', FALSE),
       ('Raqueta de Tenis Wilson', 89.99, 40, 'DEPORTE', '2025-03-18 10:04:00', '2025-03-18 10:04:00', FALSE),
       ('Chaqueta North Face', 199.99, 10, 'MODA', '2025-03-18 10:05:00', '2025-03-18 10:05:00', FALSE),
       ('Balón de Fútbol Adidas', 29.99, 50, 'DEPORTE', '2025-03-18 10:06:00', '2025-03-18 10:06:00', FALSE),
       ('Vestido de Fiesta', 159.99, 35, 'MODA', '2025-03-18 10:07:00', '2025-03-18 10:07:00', FALSE),
       ('Libro de Cocina', 24.99, 12, 'OTROS', '2025-03-18 10:08:00', '2025-03-18 10:08:00', FALSE),
       ('MacBook Air M2', 1299.99, 8, 'ELECTRONICA', '2025-03-18 10:09:00', '2025-03-18 10:09:00', FALSE);
