package dev.joseluisgs.ventas.dao

import dev.joseluisgs.ventas.models.Venta
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.customizer.BindBean
import org.jdbi.v3.sqlobject.kotlin.RegisterKotlinMapper
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.statement.SqlUpdate
import org.koin.core.annotation.Singleton
import org.lighthousegames.logging.logging
import java.util.*

@RegisterKotlinMapper(VentaEntity::class) // Indicamos que usaremos un mapper de Kotlin, cuidado con los tipos
interface VentasDao {
    
    // Empezamos con las Querys
    @SqlQuery("SELECT * FROM Ventas")
    fun findAll(): List<VentaEntity>
    
    @SqlQuery("SELECT * FROM Ventas WHERE id = :id")
    // Bind para enlazar el parametro con el valor
    fun findById(@Bind("id") id: UUID): VentaEntity?
    
    @SqlQuery("SELECT * FROM Ventas WHERE cliente_id = :clienteId")
    fun findByClienteId(@Bind("clienteId") clienteId: UUID): List<VentaEntity>
    
    @SqlUpdate("INSERT INTO Ventas (id, cliente_id, total, created_at, updated_at, deleted) VALUES (:id, :clienteId, :total, :createdAt, :updatedAt, :deleted)")
    //@GetGeneratedKeys("id") // Para obtener la clave generada que es lo que devolveremos
    // BindBean para enlazar el objeto con los parametros
    fun save(@BindBean venta: VentaEntity): Int // Ojo que ya no queremos el UUID, lo generamos nosotros, por eso es Int, porque debe devolver el número de filas afectadas
    
    @SqlUpdate("UPDATE Ventas SET cliente_id = :clienteId, total = :total, updated_at = :updatedAt WHERE id = :id")
    fun update(@BindBean venta: Venta): Int
    
    @SqlUpdate("DELETE FROM Ventas WHERE id = :id")
    fun delete(@Bind("id") id: UUID): Int
    
}

@Singleton
fun provideVentasDao(jdbi: Jdbi): VentasDao {
    val logger = logging()
    logger.debug { "Inicializando VentasDao" }
    return jdbi.onDemand(VentasDao::class.java)
}