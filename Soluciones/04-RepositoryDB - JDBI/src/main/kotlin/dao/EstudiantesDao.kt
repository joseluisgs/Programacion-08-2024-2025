package dev.joseluisgs.dao

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.customizer.BindBean
import org.jdbi.v3.sqlobject.kotlin.RegisterKotlinMapper
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.statement.SqlUpdate
import org.lighthousegames.logging.logging

/**
 * DAO para los estudiantes
 * Se indica que se va a usar un mapper de Kotlin para el Estudiante con los resultados de la consulta
 * Cuidado si los tipos no coinciden
 */
@RegisterKotlinMapper(EstudianteEntity::class) // Indicamos que usaremos un mapper de Kotlin, cuidado con los tipos
interface EstudiantesDao {
    
    // Empezamos con las Querys
    @SqlQuery("SELECT * FROM Estudiantes")
    fun findAll(): List<EstudianteEntity>
    
    // Consulta por ID, para eso usamos el :id com parametros, y el ? para indicar que puede ser nulo
    @SqlQuery("SELECT * FROM Estudiantes WHERE id = :id")
    // Bind para enlazar el parametro con el valor
    fun findById(@Bind("id") id: Int): EstudianteEntity?
    
    // Modificacioes
    @SqlUpdate("INSERT INTO Estudiantes (nombre, fecha_nacimiento, calificacion, repetidor, created_at, updated_at) VALUES (:nombre, :fechaNacimiento, :calificacion, :repetidor, :createdAt, :updatedAt)")
    @GetGeneratedKeys("id") // Para obtener la clave generada que es lo que devolveremos
    // BindBean para enlazar el objeto con los parametros
    fun save(@BindBean estudiante: EstudianteEntity): Int
    
    @SqlUpdate("UPDATE Estudiantes SET nombre = :nombre, fecha_nacimiento = :fechaNacimiento, calificacion = :calificacion, repetidor = :repetidor, updated_at = :updatedAt WHERE id = :id")
    fun update(@BindBean estudiante: EstudianteEntity): Int
    
    @SqlUpdate("DELETE FROM Estudiantes WHERE id = :id")
    fun delete(@Bind("id") id: Int): Int
    
    // Otras consultas que necesitamos
    @SqlQuery("SELECT * FROM Estudiantes LIMIT :limit OFFSET :offset")
    fun findAll(@Bind("limit") limit: Int, @Bind("offset") offset: Int): List<EstudianteEntity>
    
    @SqlQuery("SELECT * FROM Estudiantes WHERE LOWER(nombre) LIKE LOWER(:nombre)")
    fun findByName(@Bind("nombre") nombre: String): List<EstudianteEntity>
    
    @SqlQuery("SELECT * FROM Estudiantes WHERE calificacion = :calificacion")
    fun findByCalificacion(@Bind("calificacion") calificacion: Double): List<EstudianteEntity>
    
    @SqlQuery("SELECT * FROM Estudiantes WHERE repetidor = :repetidor")
    fun findByRepetidor(@Bind("repetidor") repetidor: Boolean): List<EstudianteEntity>
    
    @SqlQuery("SELECT AVG(calificacion) FROM Estudiantes")
    fun avgCalificacion(): Double
    
    @SqlQuery("SELECT COUNT(*) FROM Estudiantes")
    fun count(): Int
    
    @SqlQuery("SELECT MAX(calificacion) FROM Estudiantes")
    fun maxCalificacion(): Double
    
    @SqlQuery("SELECT MIN(calificacion) FROM Estudiantes")
    fun minCalificacion(): Double
    
    @SqlQuery("SELECT COUNT(*) FROM Estudiantes WHERE calificacion >= 5.0")
    fun countAprobados(): Int
    
    @SqlQuery("SELECT COUNT(*) FROM Estudiantes WHERE calificacion < 5.0")
    fun countSuspensos(): Int
    
}

/**
 * Función para proporcionar el DAO de Estudiantes
 */
fun provideEstudiantesDao(jdbi: Jdbi): EstudiantesDao {
    val logger = logging()
    logger.debug { "Proporcionando DAO de Estudiantes" }
    return jdbi.onDemand(EstudiantesDao::class.java)
}