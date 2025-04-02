package dev.joseluisgs.clientes.dao

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.customizer.BindBean
import org.jdbi.v3.sqlobject.kotlin.RegisterKotlinMapper
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.statement.SqlUpdate
import org.koin.core.annotation.Singleton
import org.lighthousegames.logging.logging

@RegisterKotlinMapper(ClienteEntity::class) // Indicamos que usaremos un mapper de Kotlin, cuidado con los tipos
interface ClientesDao {
    
    // Empezamos con las Querys
    @SqlQuery("SELECT * FROM Clientes")
    fun findAll(): List<ClienteEntity>
    
    @SqlQuery("SELECT * FROM Clientes WHERE id = :id")
    // Bind para enlazar el parametro con el valor
    fun findById(@Bind("id") id: Long): ClienteEntity?
    
    // Modificacioes
    @SqlUpdate("INSERT INTO Clientes (nombre, email, direcion, created_at, updated_at, deleted) VALUES (:nombre, :email, :direccion, :createdAt, :updatedAt, :deleted)")
    @GetGeneratedKeys("id") // Para obtener la clave generada que es lo que devolveremos
    // BindBean para enlazar el objeto con los parametros
    fun save(@BindBean cliente: ClienteEntity): Long
    
    @SqlUpdate("UPDATE Clientes SET nombre = :nombre, email = :email, direccion = :direccion, updated_at = :updatedAt, deleted = :deleted WHERE id = :id")
    fun update(@BindBean cliente: ClienteEntity): Int
    
    @SqlUpdate("DELETE FROM Clientes WHERE id = :id")
    fun delete(@Bind("id") id: Long): Int
    
}


@Singleton
fun provideClientesDao(jdbi: Jdbi): ClientesDao {
    val logger = logging()
    logger.debug { "Inicializando ClientesDao" }
    return jdbi.onDemand(ClientesDao::class.java)
}