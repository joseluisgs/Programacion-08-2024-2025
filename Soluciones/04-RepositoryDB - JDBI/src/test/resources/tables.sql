CREATE TABLE IF NOT EXISTS estudiantes (
   id INTEGER PRIMARY KEY,
   nombre TEXT NOT NULL,
   fecha_nacimiento TEXT NOT NULL,
   calificacion REAL NOT NULL DEFAULT 0.0,
   repetidor INTEGER NOT NULL DEFAULT 0,
   created_at TEXT NOT NULL,
   updated_at TEXT NOT NULL
);
