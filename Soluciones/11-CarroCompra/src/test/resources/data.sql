-- Para borrar
DELETE
FROM LineaVentas;

DELETE
FROM Ventas;

DELETE
FROM Productos;

DELETE
FROM Clientes;

-- Asignar valores a las columnas de las tablas autonumericos para reiniciar el contador

ALTER TABLE Productos
    ALTER COLUMN id RESTART WITH 1;

ALTER TABLE Clientes
    ALTER COLUMN id RESTART WITH 1;

-- Script para insertar datos en la tabla Productos

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

-- Script para insertar datos en la tabla Clientes

INSERT INTO Clientes (nombre, email, direccion, created_at, updated_at, deleted)
VALUES ('Cliente Test', 'juan.perez@example.com', 'Calle del Aprobado, 5. Leganés', '2025-03-18 10:10:00',
        '2025-03-18 10:10:00', false);


-- Script para insertar datos en la tabla Ventas y LineaVentas

INSERT INTO Ventas (id, total, cliente_id, created_at, updated_at, deleted)
VALUES ('1862b89e-5e86-45b7-801a-c13bc3a0b362', 1729.97, 1, '2025-03-18 10:11:00', '2025-03-18 10:11:00', false);

INSERT INTO LineaVentas (id, venta_id, producto_id, cantidad, precio, created_at, updated_at)
VALUES ('41ab73e2-0f6f-436a-8688-8b10c778d975', '1862b89e-5e86-45b7-801a-c13bc3a0b362', 1, 1, 899.99,
        '2025-03-18 10:12:00', '2025-03-18 10:12:00'),
       ('20784302-5f92-4b1b-89ed-04c890cdb60a', '1862b89e-5e86-45b7-801a-c13bc3a0b362', 3, 1, 129.99,
        '2025-03-18 10:13:00', '2025-03-18 10:13:00'),
       ('bcfee732-7ee6-42a5-8e12-0057404a1385', '1862b89e-5e86-45b7-801a-c13bc3a0b362', 4, 1, 699.99,
        '2025-03-18 10:14:00', '2025-03-18 10:14:00');