DELETE
FROM estudiantes;


ALTER TABLE estudiantes
    ALTER COLUMN id RESTART WITH 1;

INSERT INTO estudiantes (nombre, fecha_nacimiento, calificacion, repetidor, created_at, updated_at)
VALUES ('Demo', '2000-01-01', 8.5, FALSE, '2024-03-20 20:30:24.352127', '2024-03-20 20:30:24.369123');
