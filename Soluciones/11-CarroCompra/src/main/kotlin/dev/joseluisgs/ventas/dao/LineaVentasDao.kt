package dev.joseluisgs.ventas.dao

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.customizer.BindBean
import org.jdbi.v3.sqlobject.kotlin.RegisterKotlinMapper
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.statement.SqlUpdate
import org.koin.core.annotation.Singleton
import org.lighthousegames.logging.logging
import java.util.*

@RegisterKotlinMapper(LineaVentaEntity::class) // Indicamos que usaremos un mapper de Kotlin, cuidado con los tipos
interface LineaVentasDao {
    
    // Empezamos con las Querys
    @SqlQuery("SELECT * FROM LineaVentas")
    fun findAll(): List<LineaVentaEntity>
    
    @SqlQuery("SELECT * FROM LineaVentas WHERE id = :id")
    // Bind para enlazar el parametro con el valor
    fun findById(@Bind("id") id: UUID): LineaVentaEntity?
    
    @SqlQuery("SELECT * FROM LineaVentas WHERE venta_id = :ventaId")
    fun findLineasByVentaId(@Bind("ventaId") ventaId: UUID): List<LineaVentaEntity>
    
    @SqlUpdate("INSERT INTO LineaVentas (id, venta_id, producto_id, cantidad, precio, created_at, updated_at) VALUES (:id, :ventaId, :productoId, :cantidad, :precio, :createdAt, :updatedAt)")
    //@GetGeneratedKeys("id") // Para obtener la clave generada que es lo que devolveremos
    // BindBean para enlazar el objeto con los parametros
    fun save(@BindBean linea: LineaVentaEntity): Int // Ojo que ya no queremos el UUID, lo generamos nosotros, por eso es Int, porque debe devolver el número de filas afectadas
    
    @SqlUpdate("UPDATE LineaVentas SET producto_id = :productoID, cantidad = :cantidad, precio = :precio, updated_at = :updatedAt WHERE id = :id")
    fun update(@BindBean linea: LineaVentaEntity): Int
    
    @SqlUpdate("DELETE FROM LineaVentas WHERE id = :id")
    fun delete(@Bind("id") id: UUID): Int
    
}

@Singleton
fun provideLineaVentasDao(jdbi: Jdbi): LineaVentasDao {
    val logger = logging()
    logger.debug { "Inicializando LineaVentasDao" }
    return jdbi.onDemand(LineaVentasDao::class.java)
}