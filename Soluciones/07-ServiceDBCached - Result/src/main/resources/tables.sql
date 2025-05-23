CREATE TABLE IF NOT EXISTS estudiantes (
    id IDENTITY NOT NULL PRIMARY KEY,
    nombre VARCHAR NOT NULL,
    fecha_nacimiento DATE NOT NULL,
    calificacion DOUBLE NOT NULL DEFAULT 0.0,
    repetidor BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
