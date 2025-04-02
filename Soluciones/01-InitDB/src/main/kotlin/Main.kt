package dev.joseluisgs

import dev.joseluisgs.models.Alumno
import java.sql.DriverManager
import java.sql.Statement
import java.time.LocalDate


fun main() {
    // Configurar la conexion jdbc a la base de datos, en este caso SQLite con su driver jdbc y la url de la base de datos
    val databaseUrl = "jdbc:sqlite:sample.db" // Si no existe la crea en el directorio actual
    val databaseUrlMemory = "jdbc:sqlite::memory:" // Crea una base de datos en memoria
    
    
    // Conectar a la base de datos
    val connection = DriverManager.getConnection(databaseUrl)
    if (connection != null) {
        println("Conexión establecida")
    } else {
        println("Error en la conexión")
    }
    
    // Existen dos tipos de consultas: Query y Update
    // Query: SELECT
    // Update: INSERT, UPDATE, DELETE, CREATE, DROP, ALTER,...
    
    
    // Si no hay tabla la creamos
    // Cuidado, hay que saberse las sentencias SQL y los tipos de datos de cada base de datos: SQLite, MySQL, PostgreSQL, Oracle, SQLServer, H2, etc.
    val createTableSQL = """
        CREATE TABLE IF NOT EXISTS alumnado (
            id integer PRIMARY KEY,
            nombre text NOT NULL,
            calificacion real,
            aprobado integer,
            fecha_nacimiento text
        );
    """.trimIndent()
    
    // Aunque ahora lo uso nunca uses un statement, si no un prepared statement
    val statement = connection.createStatement() // Creamos un statement para ejecutar las sentencias SQL
    var res = statement.executeUpdate(createTableSQL) // Ejecutamos la sentencia SQL
    println("Tabla creada: $res")
    
    // Borrar todo, cuidado con esto
    val deleteSQL = "DELETE FROM alumnado"
    res = statement.executeUpdate(deleteSQL)
    println("Filas afectadas tras la eliminación: $res")
    
    // Insertar datos, creamos la sentencia SQL
    val insertSQL = """
        INSERT INTO alumnado (nombre, calificacion, aprobado, fecha_nacimiento)
        VALUES
        ('José Luis', 10.0, 1, '1990-01-01'),
        ('María', 9.0, 1, '1991-01-01'),
        ('Juan', 8, 1, '1992-01-01'),
        ('Ana', 3.75, false, '1993-01-01'),
        ('Pedro', 2, 0, '1994-01-01');
    """.trimIndent()
    
    res = statement.executeUpdate(insertSQL) // Ejecutamos la sentencia SQL y obtenemos el número de filas afectadas
    println("Filas afectadas tras las inserción: $res")
    
    // Consultar datos, esto es un SELECT
    val selectSQL = "SELECT * FROM alumnado"
    val resultSet =
        statement.executeQuery(selectSQL) // Ejecutamos la sentencia SQL y obtenemos el resultado de la consulta, es un ResultSet
    val alumnos = mutableListOf<Alumno>()
    
    // Recorremos el ResultSet para obtener los datos, y mapeamos los datos a un objeto Alumno
    while (resultSet.next()) {
        val id = resultSet.getLong("id") // Obtenemos el valor de la columna id
        val nombre = resultSet.getString("nombre") // Obtenemos el valor de la columna nombre
        val calificacion = resultSet.getDouble("calificacion") // Obtenemos el valor de la columna calificacion
        val aprobado = resultSet.getBoolean("aprobado") // Obtenemos el valor de la columna aprobado
        val fechaNacimiento =
            LocalDate.parse(resultSet.getString("fecha_nacimiento")) // Obtenemos el valor de la columna fecha_nacimiento
        
        alumnos.add(Alumno(id, nombre, calificacion, aprobado, fechaNacimiento)) // Añadimos el alumno a la lista
    }
    
    alumnos.forEach { println(it) } // Mostramos los alumnos
    
    // Vamos a insertar un alumno
    val alumno = Alumno(
        nombre = "Javi",
        calificacion = 4.0,
        aprobado = false,
        fechaNacimiento = LocalDate.now()
    )
    
    val insertAlumnoSQL = """
        INSERT INTO alumnado (nombre, calificacion, aprobado, fecha_nacimiento)
        VALUES
        ('${alumno.nombre}', ${alumno.calificacion}, ${alumno.aprobado}, '${alumno.fechaNacimiento}');
    """.trimIndent()
    
    val resInsert =
        statement.executeUpdate(insertAlumnoSQL) // Ejecutamos la sentencia SQL y obtenemos el número de filas afectadas
    println("Filas afectadas tras la inserción: $resInsert")
    
    // Select max(id) from alumnado --> CACA lo recorre todo
    val lastId =
        statement.executeQuery("SELECT last_insert_rowid()") // Obtenemos el último id insertado, pero y si han insertado 40 entre medias?
    val id = lastId.getLong(1)
    println("El id del último alumno insertado es: $id")
    
    // Consultar datos de los alumnos que se llaman Javi
    val selectJaviSQL = "SELECT * FROM alumnado WHERE nombre = 'Javi'"
    val resultSetJavi = statement.executeQuery(selectJaviSQL)
    val alumnosJavi = mutableListOf<Alumno>()
    while (resultSetJavi.next()) {
        val id = resultSetJavi.getLong("id")
        val nombre = resultSetJavi.getString("nombre")
        val calificacion = resultSetJavi.getDouble("calificacion")
        val aprobado = resultSetJavi.getBoolean("aprobado")
        val fechaNacimiento = LocalDate.parse(resultSetJavi.getString("fecha_nacimiento"))
        alumnosJavi.add(Alumno(id, nombre, calificacion, aprobado, fechaNacimiento))
    }
    alumnosJavi.forEach { println(it) }
    
    // Alumnos con id = 6
    val selectIdSQL = "SELECT * FROM alumnado WHERE id = 6"
    val resultSetId = statement.executeQuery(selectIdSQL)
    val alumnosId = mutableListOf<Alumno>()
    while (resultSetId.next()) {
        val id = resultSetId.getLong("id")
        val nombre = resultSetId.getString("nombre")
        val calificacion = resultSetId.getDouble("calificacion")
        val aprobado = resultSetId.getBoolean("aprobado")
        val fechaNacimiento = LocalDate.parse(resultSetId.getString("fecha_nacimiento"))
        alumnosId.add(Alumno(id, nombre, calificacion, aprobado, fechaNacimiento))
    }
    val myAlumno = alumnosId.first()
    println("El alumno con id 6 es: $myAlumno")
    
    // Vamos a actualizar un alumno
    val alumnoUpdate = myAlumno.copy(calificacion = 5.0, aprobado = true)
    val updateAlumnoSQL = """
        UPDATE alumnado
        SET calificacion = ${alumnoUpdate.calificacion}, aprobado = ${alumnoUpdate.aprobado}
        WHERE id = ${alumnoUpdate.id};
    """.trimIndent()
    val resUpdate = statement.executeUpdate(updateAlumnoSQL)
    println("Filas afectadas tras la actualización: $resUpdate")
    
    // Todos los alumnos
    val selectAllSQL = "SELECT * FROM alumnado"
    val resultSetAll = statement.executeQuery(selectAllSQL)
    val alumnosAll = mutableListOf<Alumno>()
    while (resultSetAll.next()) {
        val id = resultSetAll.getLong("id")
        val nombre = resultSetAll.getString("nombre")
        val calificacion = resultSetAll.getDouble("calificacion")
        val aprobado = resultSetAll.getBoolean("aprobado")
        val fechaNacimiento = LocalDate.parse(resultSetAll.getString("fecha_nacimiento"))
        alumnosAll.add(Alumno(id, nombre, calificacion, aprobado, fechaNacimiento))
    }
    alumnosAll.forEach { println(it) }
    
    // Borrar un alumno
    val deleteAlumnoSQL = "DELETE FROM alumnado WHERE id = 6"
    val resDelete = statement.executeUpdate(deleteAlumnoSQL)
    println("Filas afectadas tras la eliminación: $resDelete")
    
    // Todos los alumnos
    val resultSetAll2 = statement.executeQuery(selectAllSQL)
    val alumnosAll2 = mutableListOf<Alumno>()
    while (resultSetAll2.next()) {
        val id = resultSetAll2.getLong("id")
        val nombre = resultSetAll2.getString("nombre")
        val calificacion = resultSetAll2.getDouble("calificacion")
        val aprobado = resultSetAll2.getBoolean("aprobado")
        val fechaNacimiento = LocalDate.parse(resultSetAll2.getString("fecha_nacimiento"))
        alumnosAll2.add(Alumno(id, nombre, calificacion, aprobado, fechaNacimiento))
    }
    alumnosAll2.forEach { println(it) }
    
    // Saber cuantos alumnos hay
    val countAlumnosSQL = "SELECT COUNT(*) FROM alumnado"
    val resultSetCount = statement.executeQuery(countAlumnosSQL)
    val count = resultSetCount.getInt(1)
    println("Hay $count alumnos")
    
    // Cuantos alumnos aprobados hay
    val countAprobadosSQL = "SELECT COUNT(*) FROM alumnado WHERE aprobado = 1"
    val resultSetAprobados = statement.executeQuery(countAprobadosSQL)
    val countAprobados = resultSetAprobados.getInt(1)
    println("Hay $countAprobados alumnos aprobados")
    
    // Media de calificaciones
    val avgCalificacionesSQL = "SELECT AVG(calificacion) FROM alumnado"
    val resultSetAvg = statement.executeQuery(avgCalificacionesSQL)
    val avg = resultSetAvg.getDouble(1)
    println("La media de calificaciones es: $avg")
    
    // Calificación máxima
    val maxCalificacionesSQL = "SELECT MAX(calificacion) FROM alumnado"
    val resultSetMax = statement.executeQuery(maxCalificacionesSQL)
    val max = resultSetMax.getDouble(1)
    println("La máxima calificación es: $max")
    
    
    // Cómo hay que hacer las cosaa sbien con preparedstatement y return generated keys
    
    val alumno4 = Alumno(
        nombre = "Javi",
        calificacion = 4.0,
        aprobado = false,
        fechaNacimiento = LocalDate.now()
    )
    val insertAlumnoPreparedSQL = """
      INSERT INTO alumnado (nombre, calificacion, aprobado, fecha_nacimiento)
        VALUES
        (?, ?, ?, ?)
    """.trimIndent()
    
    val preparedStatement = connection.prepareStatement(
        insertAlumnoPreparedSQL,
        Statement.RETURN_GENERATED_KEYS
    ) // Creamos un PreparedStatement con la sentencia y la flag de RETURN_GENERATED_KEYS para obtener el id generado
    
    preparedStatement.setString(1, alumno4.nombre) // Sustituimos el primer ? por el nombre
    preparedStatement.setDouble(2, alumno4.calificacion) // Sustituimos el segundo ? por la calificación
    preparedStatement.setBoolean(3, alumno4.aprobado) // Sustituimos el tercer ? por si está aprobado
    preparedStatement.setString(
        4,
        alumno4.fechaNacimiento.toString()
    ) // Sustituimos el cuarto ? por la fecha de nacimiento
    
    var idJavi: Long = -1 // Variable para almacenar el id del alumno insertado
    
    preparedStatement.executeUpdate() // Ejecutamos la sentencia
    val generatedKeys = preparedStatement.generatedKeys // Obtenemos las claves generadas
    if (generatedKeys.next()) {
        idJavi = generatedKeys.getLong(1) // Obtenemos el id generado
        println("El id del último alumno insertado es: $id")
    }
    
    // Consultar datos de los alumnos que tiene ese id
    val selectIdJaviSQL = "SELECT * FROM alumnado WHERE id = ?"
    val preparedStatementId = connection.prepareStatement(selectIdJaviSQL)
    preparedStatementId.setLong(1, idJavi)
    val resultSetIdJavi = preparedStatementId.executeQuery()
    
    val alumnosIdJavi = mutableListOf<Alumno>()
    while (resultSetIdJavi.next()) {
        val id = resultSetIdJavi.getLong("id")
        val nombre = resultSetIdJavi.getString("nombre")
        val calificacion = resultSetIdJavi.getDouble("calificacion")
        val aprobado = resultSetIdJavi.getBoolean("aprobado")
        val fechaNacimiento = LocalDate.parse(resultSetIdJavi.getString("fecha_nacimiento"))
        alumnosIdJavi.add(Alumno(id, nombre, calificacion, aprobado, fechaNacimiento))
    }
    val myAlumnoJavi = alumnosIdJavi.first()
    println("El alumno con id $idJavi es: $myAlumnoJavi")
    
    // Cerrar la conexión
    connection.close()
    
    
}