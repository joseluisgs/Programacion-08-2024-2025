package dev.joseluisgs.repositories.estudiantes

import database.DataBaseManager
import dev.joseluisgs.models.Estudiante
import org.lighthousegames.logging.logging
import java.sql.Statement
import java.time.LocalDate
import java.time.LocalDateTime


class EstudiantesRepositoryImpl : EstudiantesRepository {
    private val logger = logging()
    
    override fun findAll(): List<Estudiante> {
        logger.debug { "Buscando todos los estudiantes" }
        val estudiantes = mutableListOf<Estudiante>()
        
        val sql = "SELECT * FROM Estudiantes"
        
        // Aquí iría la consulta a la base de datos
        DataBaseManager.use { db ->
            val result = db.connection?.prepareStatement(sql)!!.executeQuery()
            // Procesamos el resultado
            while (result.next()) {
                val estudiante = Estudiante(
                    id = result.getLong("id"),
                    nombre = result.getString("nombre"),
                    fechaNacimiento = LocalDate.parse(result.getString("fecha_nacimiento")),
                    calificacion = result.getDouble("calificacion"),
                    repetidor = result.getBoolean("repetidor"),
                    createdAt = LocalDateTime.parse(result.getString("created_at")),
                    updatedAt = LocalDateTime.parse(result.getString("updated_at"))
                )
                // Añadimos el estudiante a la lista
                estudiantes.add(estudiante)
            }
        }
        return estudiantes.toList()
    }
    
    override fun findById(id: Long): Estudiante? {
        logger.debug { "Buscando estudiante por id: $id" }
        var estudiante: Estudiante? = null
        
        val sql = "SELECT * FROM Estudiantes WHERE id = ?"
        
        // Aquí iría la consulta a la base de datos
        DataBaseManager.use { db ->
            // Le pasamos los parámetros a la consulta
            val statement = db.connection?.prepareStatement(sql)!!
            statement.setLong(1, id)
            
            // Ejecutamos la consulta
            val result = statement.executeQuery()
            // Procesamos el resultado
            if (result.next()) {
                estudiante = Estudiante(
                    id = result.getLong("id"),
                    nombre = result.getString("nombre"),
                    fechaNacimiento = LocalDate.parse(result.getString("fecha_nacimiento")),
                    calificacion = result.getDouble("calificacion"),
                    repetidor = result.getBoolean("repetidor"),
                    createdAt = LocalDateTime.parse(result.getString("created_at")),
                    updatedAt = LocalDateTime.parse(result.getString("updated_at"))
                )
            }
        }
        return estudiante
    }
    
    override fun save(item: Estudiante): Estudiante {
        logger.debug { "Guardando estudiante: $item" }
        var estudiante: Estudiante? = null
        val timeStamp = LocalDateTime.now()
        
        val sql = """
            INSERT INTO Estudiantes (nombre, fecha_nacimiento, calificacion, repetidor, created_at, updated_at)
            VALUES (?, ?, ?, ?, ?, ?)
        """.trimIndent()
        
        // Aquí iría la consulta a la base de datos
        DataBaseManager.use { db ->
            // Le pasamos los parámetros a la consulta,
            val statement = db.connection?.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)!!
            statement.setString(1, item.nombre)
            statement.setString(2, item.fechaNacimiento.toString())
            statement.setDouble(3, item.calificacion)
            statement.setBoolean(4, item.repetidor)
            statement.setString(5, timeStamp.toString())
            statement.setString(6, timeStamp.toString())
            
            // Ejecutamos la consulta
            statement.executeUpdate()
            // Procesamos el id en item
            estudiante = item.copy(
                id = statement.generatedKeys.getLong(1),
                createdAt = timeStamp,
                updatedAt = timeStamp
            )
        }
        return estudiante!!
    }
    
    override fun update(id: Long, item: Estudiante): Estudiante? {
        logger.debug { "Actualizando estudiante por id: $id" }
        var estudiante: Estudiante? = this.findById(id)
        val timeStamp = LocalDateTime.now()
        
        if (estudiante != null) {
            val sql = """
                UPDATE Estudiantes
                SET nombre = ?, fecha_nacimiento = ?, calificacion = ?, repetidor = ?, updated_at = ?
                WHERE id = ?
            """.trimIndent()
            
            // Aquí iría la consulta a la base de datos
            DataBaseManager.use { db ->
                // Le pasamos los parámetros a la consulta,
                val statement = db.connection?.prepareStatement(sql)!!
                statement.setString(1, item.nombre)
                statement.setString(2, item.fechaNacimiento.toString())
                statement.setDouble(3, item.calificacion)
                statement.setBoolean(4, item.repetidor)
                statement.setString(5, timeStamp.toString())
                statement.setLong(6, id)
                
                // Ejecutamos la consulta
                statement.executeUpdate()
                estudiante = item.copy(
                    id = id,
                    nombre = item.nombre,
                    fechaNacimiento = item.fechaNacimiento,
                    calificacion = item.calificacion,
                    repetidor = item.repetidor,
                    createdAt = estudiante!!.createdAt,
                    updatedAt = timeStamp
                )
            }
        }
        return estudiante
    }
    
    override fun delete(id: Long): Estudiante? {
        logger.debug { "Borrando estudiante por id: $id" }
        val estudiante: Estudiante? = findById(id)
        if (estudiante != null) {
            val sql = "DELETE FROM Estudiantes WHERE id = ?"
            
            // Aquí iría la consulta a la base de datos
            DataBaseManager.use { db ->
                // Le pasamos los parámetros a la consulta,
                val statement = db.connection?.prepareStatement(sql)!!
                statement.setLong(1, id)
                
                // Ejecutamos la consulta
                statement.executeUpdate()
            }
        }
        return estudiante?.copy(updatedAt = LocalDateTime.now())
    }
    
    override fun findAllPaginated(page: Int, size: Int): List<Estudiante> {
        logger.debug { "Buscando todos los estudiantes paginados, página: $page, tamaño: $size" }
        val myPage = if (page < 1) 0 else page - 1
        val mySize = if (size < 1) 10 else size
        // Dos opciones con todo y windowed
        // return findAll().windowed(myPage, mySize, true)[myPage]
        // Opción con la consulta a la base de datos
        val estudiantes = mutableListOf<Estudiante>()
        val sql = "SELECT * FROM Estudiantes LIMIT ? OFFSET ?"
        DataBaseManager.use { db ->
            // Le pasamos los parámetros a la consulta
            val statement = db.connection?.prepareStatement(sql)!!
            statement.setInt(1, mySize)
            statement.setInt(2, (myPage) * mySize)
            
            // Ejecutamos la consulta
            val result = statement.executeQuery()
            // Procesamos el resultado
            while (result.next()) {
                val estudiante = Estudiante(
                    id = result.getLong("id"),
                    nombre = result.getString("nombre"),
                    fechaNacimiento = LocalDate.parse(result.getString("fecha_nacimiento")),
                    calificacion = result.getDouble("calificacion"),
                    repetidor = result.getBoolean("repetidor"),
                    createdAt = LocalDateTime.parse(result.getString("created_at")),
                    updatedAt = LocalDateTime.parse(result.getString("updated_at"))
                )
                // Añadimos el estudiante a la lista
                estudiantes.add(estudiante)
            }
        }
        return estudiantes.toList()
    }
    
    override fun findByNombre(nombre: String): List<Estudiante> {
        logger.debug { "Buscando estudiantes por nombre: $nombre" }
        // Dos opciones con todo y el filter
        // return findAll().filter { it.nombre.contains(nombre, ignoreCase = true) }
        // Opción con la consulta a la base de datos con LIKE y %nombre% ignorando mayúsculas
        val estudiantes = mutableListOf<Estudiante>()
        
        val sql = "SELECT * FROM Estudiantes WHERE LOWER(nombre) LIKE LOWER(?)"
        
        // Aquí iría la consulta a la base de datos
        DataBaseManager.use { db ->
            // Le pasamos los parámetros a la consulta
            val statement = db.connection?.prepareStatement(sql)!!
            statement.setString(1, "%$nombre%")
            
            // Ejecutamos la consulta
            val result = statement.executeQuery()
            // Procesamos el resultado
            while (result.next()) {
                val estudiante = Estudiante(
                    id = result.getLong("id"),
                    nombre = result.getString("nombre"),
                    fechaNacimiento = LocalDate.parse(result.getString("fecha_nacimiento")),
                    calificacion = result.getDouble("calificacion"),
                    repetidor = result.getBoolean("repetidor"),
                    createdAt = LocalDateTime.parse(result.getString("created_at")),
                    updatedAt = LocalDateTime.parse(result.getString("updated_at"))
                )
                // Añadimos el estudiante a la lista
                estudiantes.add(estudiante)
            }
        }
        return estudiantes.toList()
    }
    
    override fun findByCalificacion(calificacion: Double): List<Estudiante> {
        logger.debug { "Buscando estudiantes por calificación: $calificacion" }
        // Dos opciones con todo y el filter
        // return findAll().filter { it.calificacion == calificacion }
        // Opción con la consulta a la base de datos
        val estudiantes = mutableListOf<Estudiante>()
        
        val sql = "SELECT * FROM Estudiantes WHERE calificacion = ?"
        
        // Aquí iría la consulta a la base de datos
        DataBaseManager.use { db ->
            // Le pasamos los parámetros a la consulta
            val statement = db.connection?.prepareStatement(sql)!!
            statement.setDouble(1, calificacion)
            
            // Ejecutamos la consulta
            val result = statement.executeQuery()
            // Procesamos el resultado
            while (result.next()) {
                val estudiante = Estudiante(
                    id = result.getLong("id"),
                    nombre = result.getString("nombre"),
                    fechaNacimiento = LocalDate.parse(result.getString("fecha_nacimiento")),
                    calificacion = result.getDouble("calificacion"),
                    repetidor = result.getBoolean("repetidor"),
                    createdAt = LocalDateTime.parse(result.getString("created_at")),
                    updatedAt = LocalDateTime.parse(result.getString("updated_at"))
                )
                // Añadimos el estudiante a la lista
                estudiantes.add(estudiante)
            }
        }
        return estudiantes.toList()
    }
    
    override fun findByRepetidor(repetidor: Boolean): List<Estudiante> {
        logger.debug { "Buscando estudiantes por repetidor: $repetidor" }
        // Dos opciones con todo y el filter
        // return findAll().filter { it.repetidor == repetidor }
        // Opción con la consulta a la base de datos
        val estudiantes = mutableListOf<Estudiante>()
        
        val sql = "SELECT * FROM Estudiantes WHERE repetidor =?"
        
        // Aquí iría la consulta a la base de datos
        DataBaseManager.use { db ->
            // Le pasamos los parámetros a la consulta
            val statement = db.connection?.prepareStatement(sql)!!
            statement.setBoolean(1, repetidor)
            
            // Ejecutamos la consulta
            val result = statement.executeQuery()
            // Procesamos el resultado
            while (result.next()) {
                val estudiante = Estudiante(
                    id = result.getLong("id"),
                    nombre = result.getString("nombre"),
                    fechaNacimiento = LocalDate.parse(result.getString("fecha_nacimiento")),
                    calificacion = result.getDouble("calificacion"),
                    repetidor = result.getBoolean("repetidor"),
                    createdAt = LocalDateTime.parse(result.getString("created_at")),
                    updatedAt = LocalDateTime.parse(result.getString("updated_at"))
                )
                // Añadimos el estudiante a la lista
                estudiantes.add(estudiante)
            }
        }
        return estudiantes.toList()
    }
    
    override fun mediaCalificaciones(): Double {
        logger.debug { "Calculando media de calificaciones" }
        // Dos opciones con todo y average
        // return findAll().map { it.calificacion }.average()
        // Opción con la consulta a la base de datos
        var media = 0.0
        val sql = "SELECT AVG(calificacion) FROM Estudiantes"
        DataBaseManager.use { db ->
            val result = db.connection?.prepareStatement(sql)!!.executeQuery()
            if (result.next()) {
                media = result.getDouble(1)
            }
        }
        return media
    }
    
    override fun maximaCalificacion(): Double {
        logger.debug { "Calculando máxima calificación" }
        // Dos opciones con todo y max
        // return findAll().map { it.calificacion }.maxOrNull() ?: 0.0
        // Opción con la consulta a la base de datos
        var maxima = 0.0
        val sql = "SELECT MAX(calificacion) FROM Estudiantes"
        DataBaseManager.use { db ->
            val result = db.connection?.prepareStatement(sql)!!.executeQuery()
            if (result.next()) {
                maxima = result.getDouble(1)
            }
        }
        return maxima
    }
    
    override fun minimaCalificacion(): Double {
        logger.debug { "Calculando mínima calificación" }
        // Dos opciones con todo y min
        // return findAll().map { it.calificacion }.minOrNull() ?: 0.0
        // Opción con la consulta a la base de datos
        var minima = 0.0
        val sql = "SELECT MIN(calificacion) FROM Estudiantes"
        DataBaseManager.use { db ->
            val result = db.connection?.prepareStatement(sql)!!.executeQuery()
            if (result.next()) {
                minima = result.getDouble(1)
            }
        }
        return minima
    }
    
    override fun numAprobados(): Int {
        logger.debug { "Calculando número de aprobados" }
        // Dos opciones con todo y count
        // return findAll().count { it.calificacion >= 5.0 }
        // Opción con la consulta a la base de datos
        var aprobados = 0
        val sql = "SELECT COUNT(*) FROM Estudiantes WHERE calificacion >= 5.0"
        DataBaseManager.use { db ->
            val result = db.connection?.prepareStatement(sql)!!.executeQuery()
            if (result.next()) {
                aprobados = result.getInt(1)
            }
        }
        return aprobados
    }
    
    override fun numSuspensos(): Int {
        logger.debug { "Calculando número de suspensos" }
        // Dos opciones con todo y count
        // return findAll().count { it.calificacion < 5.0 }
        // Opción con la consulta a la base de datos
        var suspensos = 0
        val sql = "SELECT COUNT(*) FROM Estudiantes WHERE calificacion < 5.0"
        DataBaseManager.use { db ->
            val result = db.connection?.prepareStatement(sql)!!.executeQuery()
            if (result.next()) {
                suspensos = result.getInt(1)
            }
        }
        return suspensos
    }
    
}