package dev.joseluisgs.productos.dao

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.customizer.BindBean
import org.jdbi.v3.sqlobject.kotlin.RegisterKotlinMapper
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.statement.SqlUpdate
import org.koin.core.annotation.Singleton
import org.lighthousegames.logging.logging

/**
 * DAO para los Productos
 * Se indica que se va a usar un mapper de Kotlin para el Estudiante con los resultados de la consulta
 * Cuidado si los tipos no coinciden
 */
@RegisterKotlinMapper(ProductoEntity::class) // Indicamos que usaremos un mapper de Kotlin, cuidado con los tipos
interface ProductosDao {
    
    // Empezamos con las Querys
    @SqlQuery("SELECT * FROM Productos")
    fun findAll(): List<ProductoEntity>
    
    // Consulta por ID, para eso usamos el :id com parametros, y el ? para indicar que puede ser nulo
    @SqlQuery("SELECT * FROM Productos WHERE id = :id")
    // Bind para enlazar el parametro con el valor
    fun findById(@Bind("id") id: Long): ProductoEntity?
    
    // Modificacioes
    @SqlUpdate("INSERT INTO Productos (nombre, precio, stock, categoria, created_at, updated_at, deleted) VALUES (:nombre, :precio, :stock, :categoria, :createdAt, :updatedAt, :deleted)")
    @GetGeneratedKeys("id") // Para obtener la clave generada que es lo que devolveremos
    // BindBean para enlazar el objeto con los parametros
    fun save(@BindBean producto: ProductoEntity): Long
    
    @SqlUpdate("UPDATE Productos SET nombre = :nombre, precio = :precio, stock = :stock, categoria = :categoria, updated_at = :updatedAt, deleted = :deleted WHERE id = :id")
    fun update(@BindBean producto: ProductoEntity): Int
    
    @SqlUpdate("DELETE FROM Productos WHERE id = :id")
    fun delete(@Bind("id") id: Long): Int
    
    // Otras consultas que necesitamos
    @SqlQuery("SELECT * FROM Productos LIMIT :limit OFFSET :offset")
    fun findAll(@Bind("limit") limit: Int, @Bind("offset") offset: Int): List<ProductoEntity>
    
    @SqlQuery("SELECT * FROM Productos WHERE LOWER(nombre) LIKE LOWER(:nombre)")
    fun findByName(@Bind("nombre") nombre: String): List<ProductoEntity>
    
    @SqlQuery("SELECT * FROM Productos WHERE LOWER(categoria) LIKE LOWER(:categoria)")
    fun findByCategoria(@Bind("categoria") categoria: String): List<ProductoEntity>
}

@Singleton
fun provideProductosDao(jdbi: Jdbi): ProductosDao {
    val logger = logging()
    logger.debug { "Inicializando ProductoDao" }
    return jdbi.onDemand(ProductosDao::class.java)
}