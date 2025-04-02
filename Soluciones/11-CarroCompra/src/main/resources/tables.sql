CREATE TABLE IF NOT EXISTS Productos (
      id BIGINT PRIMARY KEY,
      nombre TEXT NOT NULL,
      precio REAL NOT NULL,
      stock INTEGER NOT NULL,
      categoria TEXT NOT NULL,
      created_at TIMESTAMP NOT NULL,
      updated_at TIMESTAMP NOT NULL,
      deleted BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS Clientes (
      id BIGINT PRIMARY KEY,
      nombre TEXT NOT NULL,
      email TEXT NOT NULL,
      direccion TEXT NOT NULL,
      created_at TIMESTAMP NOT NULL,
      updated_at TIMESTAMP NOT NULL,
      deleted BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS Ventas(
      id UUID PRIMARY KEY,
      total REAL NOT NULL,
      cliente_id BIGINT NOT NULL REFERENCES Clientes(id),
      created_at TIMESTAMP NOT NULL,
      updated_at TIMESTAMP NOT NULL,
      deleted BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS LineaVentas (
      id UUID PRIMARY KEY,
      venta_id UUID NOT NULL REFERENCES Ventas(id),
      producto_id BIGINT NOT NULL REFERENCES Productos(id),
      cantidad INTEGER NOT NULL,
      precio REAL NOT NULL,
      created_at TIMESTAMP NOT NULL,
      updated_at TIMESTAMP NOT NULL
);
