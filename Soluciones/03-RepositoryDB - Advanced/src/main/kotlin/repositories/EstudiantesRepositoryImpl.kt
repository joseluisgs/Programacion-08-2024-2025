package dev.joseluisgs.repositories

import database.DatabaseService
import dev.joseluisgs.database.DatabaseManager
import dev.joseluisgs.models.Estudiante
import org.lighthousegames.logging.logging
import java.time.LocalDateTime

typealias EstudianteEntity = Map<String, Any>


class EstudiantesRepositoryImpl(
    private val db: DatabaseManager = DatabaseService()
) : EstudiantesRepository {
    private val logger = logging()
    
    override fun findAll(): List<Estudiante> {
        logger.debug { "Buscando todos los estudiantes" }
        
        val sql = "SELECT * FROM Estudiantes"
        
        // Y si lo hemos hecho bien podemos hacer
        return db.select(sql).map { it.toModel() }
        
    }
    
    
    override fun findById(id: Int): Estudiante? {
        logger.debug { "Buscando estudiante por id: $id" }
        // var estudiante: Estudiante? = null
        
        val sql = "SELECT * FROM Estudiantes WHERE id = ?"
        
        
        return db.select(sql, id).map { it.toModel() }.firstOrNull()
        
    }
    
    override fun save(item: Estudiante): Estudiante {
        logger.debug { "Guardando estudiante: $item" }
        lateinit var estudiante: Estudiante
        val timeStamp = LocalDateTime.now()
        
        val sql = """
            INSERT INTO Estudiantes (nombre, fecha_nacimiento, calificacion, repetidor, created_at, updated_at)
            VALUES (?, ?, ?, ?, ?, ?)
        """.trimIndent()
        
        // Aquí iría la consulta a la base de datos
        /* db.insertAndGetId(
             sql,
             item.nombre,
             item.fechaNacimiento.toString(),
             item.calificacion,
             item.repetidor,
             timeStamp,
             timeStamp
         ).let {
             estudiante = item.copy(
                 id = it as Long,
                 nombre = item.nombre,
                 fechaNacimiento = item.fechaNacimiento,
                 calificacion = item.calificacion,
                 repetidor = item.repetidor,
                 createdAt = timeStamp,
                 updatedAt = timeStamp
             )
         }*/
        
        val result = db.insertAndGetId(
            sql,
            item.nombre,
            item.fechaNacimiento.toString(),
            item.calificacion,
            item.repetidor,
            timeStamp,
            timeStamp
        )
        
        estudiante = item.copy(
            id = result as Int, // Aquí se hace el cast de la clave generada, por si no es un Long o un UUID
            createdAt = timeStamp,
            updatedAt = timeStamp
        )
        
        return estudiante
    }
    
    override fun update(id: Int, item: Estudiante): Estudiante? {
        logger.debug { "Actualizando estudiante por id: $id" }
        var estudiante: Estudiante? = this.findById(id)
        val timeStamp = LocalDateTime.now()
        
        if (estudiante != null) {
            val sql = """
                UPDATE Estudiantes
                SET nombre = ?, fecha_nacimiento = ?, calificacion = ?, repetidor = ?, updated_at = ?
                WHERE id = ?
            """.trimIndent()
            
            val result = db.update(
                sql,
                item.nombre,
                item.fechaNacimiento.toString(),
                item.calificacion,
                item.repetidor,
                timeStamp,
                id
            )
            
            estudiante = item.copy(
                id = id,
                nombre = item.nombre,
                fechaNacimiento = item.fechaNacimiento,
                calificacion = item.calificacion,
                repetidor = item.repetidor,
                createdAt = item.createdAt,
                updatedAt = timeStamp
            )
            
        }
        return estudiante
    }
    
    override fun delete(id: Int): Estudiante? {
        logger.debug { "Borrando estudiante por id: $id" }
        var estudiante: Estudiante? = findById(id)
        if (estudiante != null) {
            val sql = "DELETE FROM Estudiantes WHERE id = ?"
            
            val result = db.delete(sql, id)
            
            estudiante = estudiante.copy(updatedAt = LocalDateTime.now())
        }
        return estudiante
    }
    
    override fun findAllPaginated(page: Int, size: Int): List<Estudiante> {
        logger.debug { "Buscando todos los estudiantes paginados, página: $page, tamaño: $size" }
        val myPage = if (page < 1) 0 else page - 1 // Cuidado con la paginación y los índices para test
        val mySize = if (size < 1) 10 else size
        // Dos opciones con todo y windowed
        // return findAll().windowed(myPage, mySize, true)[myPage]
        // Opción con la consulta a la base de datos
        val sql = "SELECT * FROM Estudiantes LIMIT ? OFFSET ?"
        
        // Aquí iría la consulta a la base de datos, cuidado con los índices para los test
        return db.select(sql, mySize, myPage * mySize).map { it.toModel() }
    }
    
    override fun findByNombre(nombre: String): List<Estudiante> {
        logger.debug { "Buscando estudiantes por nombre: $nombre" }
        
        val sql = "SELECT * FROM Estudiantes WHERE LOWER(nombre) LIKE LOWER(?)"
        
        return db.select(sql, "%$nombre%").map { it.toModel() }
    }
    
    override fun findByCalificacion(calificacion: Double): List<Estudiante> {
        logger.debug { "Buscando estudiantes por calificación: $calificacion" }
        
        val sql = "SELECT * FROM Estudiantes WHERE calificacion = ?"
        
        // Aquí iría la consulta a la base de datos
        return db.select(sql, calificacion).map { it.toModel() }
    }
    
    override fun findByRepetidor(repetidor: Boolean): List<Estudiante> {
        logger.debug { "Buscando estudiantes por repetidor: $repetidor" }
        
        val sql = "SELECT * FROM Estudiantes WHERE repetidor =?"
        
        // Aquí iría la consulta a la base de datos
        return db.select(sql, repetidor).map { it.toModel() }
    }
    
    override fun mediaCalificaciones(): Double {
        logger.debug { "Calculando media de calificaciones" }
        // Dos opciones con todo y average
        // return findAll().map { it.calificacion }.average()
        // Opción con la consulta a la base de datos
        var media = 0.0
        val sql = "SELECT AVG(calificacion) FROM Estudiantes"
        val result = db.select(sql)
        if (result.isNotEmpty()) {
            media = result[0].values.first() as Double
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
        val result = db.select(sql)
        if (result.isNotEmpty()) {
            maxima = result[0].values.first() as Double
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
        val result = db.select(sql)
        if (result.isNotEmpty()) {
            minima = result[0].values.first() as Double
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
        val result = db.select(sql)
        if (result.isNotEmpty()) {
            aprobados = result[0].values.first() as Int
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
        val result = db.select(sql)
        if (result.isNotEmpty()) {
            suspensos = result[0].values.first() as Int
        }
        return suspensos
    }
    
    
    fun EstudianteEntity.toModel(): Estudiante {
        return Estudiante(
            id = this["id"] as Int,
            nombre = this["nombre"] as String,
            fechaNacimiento = java.time.LocalDate.parse(this["fecha_nacimiento"] as String),
            calificacion = this["calificacion"] as Double,
            repetidor = this["repetidor"] as Int == 1,
            createdAt = LocalDateTime.parse(this["created_at"] as String),
            updatedAt = LocalDateTime.parse(this["updated_at"] as String)
        )
    }
    
}