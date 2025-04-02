package dev.joseluisgs.ventas.repository

import dev.joseluisgs.clientes.dao.ClientesDao
import dev.joseluisgs.clientes.mapper.toModel
import dev.joseluisgs.productos.dao.ProductosDao
import dev.joseluisgs.productos.mapper.toModel
import dev.joseluisgs.ventas.dao.LineaVentasDao
import dev.joseluisgs.ventas.dao.VentasDao
import dev.joseluisgs.ventas.mapper.toEntity
import dev.joseluisgs.ventas.mapper.toModel
import dev.joseluisgs.ventas.models.Venta
import org.koin.core.annotation.Singleton
import org.lighthousegames.logging.logging
import java.util.*

@Singleton
class VentasRepositoryImpl(
    private val daoVentas: VentasDao,
    private val daoLineas: LineaVentasDao,
    private val clientesDao: ClientesDao,
    private val productosDao: ProductosDao,
) : VentasRepository {
    private val logger = logging()
    
    
    override fun findAll(): List<Venta> {
        // Buscamos todas las ventasEntity
        val ventasEntity = daoVentas.findAll()
        logger.debug { "Buscando todas las ventas" }
        if (ventasEntity.isEmpty()) {
            logger.error { "No se han encontrado ventas" }
            return emptyList()
        }
        // Mapeamos las ventasEntity a modelos
        // Por cada ventaEntity buscamos el cliente y las lineas de venta
        val ventas = ventasEntity.map { ventaEntity ->
            // Buscamos el cliente
            val cliente = clientesDao.findById(ventaEntity.clienteId)
            if (cliente == null) {
                logger.error { "Cliente con id: ${ventaEntity.clienteId} asociado en la venta no encontrado" }
                return@map null
            }
            // Buscamos las lineas de venta
            val lineas = daoLineas.findLineasByVentaId(ventaEntity.id)
            // Mapeamos las lineas de venta a modelos, mapeando con el producto
            val lineasVenta = lineas.map {
                val producto = productosDao.findById(it.productoId)
                if (producto == null) {
                    logger.error { "Producto con id: ${it.productoId} asociado a la linea no encontrado" }
                }
                it.toModel(producto!!.toModel())
            }
            // Mapeamos la venta
            ventaEntity.toModel(cliente.toModel(), lineasVenta)
        }.filterNotNull()
        logger.debug { "Ventas encontradas: $ventas" }
        return ventas
    }
    
    override fun findById(id: UUID): Venta? {
        // Buscamos la ventaEntity por su id
        logger.debug { "Buscando venta por id: $id" }
        val ventaEntity = daoVentas.findById(id)
        if (ventaEntity == null) {
            logger.error { "Venta no encontrada" }
            return null
        }
        logger.debug { "Venta encontrada: $ventaEntity" }
        // Buscamos las lineas de venta
        val lineas = daoLineas.findLineasByVentaId(id)
        // Buscamos el cliente
        val cliente = clientesDao.findById(ventaEntity.clienteId)
        if (cliente == null) {
            logger.error { "Cliente con id: ${ventaEntity.clienteId} asociado en la venta no encontrado" }
            return null
        }
        // Mapeamos las lineas de venta a modelos, mapeando con el producto
        val lineasVenta = lineas.map {
            val producto = productosDao.findById(it.productoId)
            if (producto == null) {
                logger.error { "Producto con id: ${it.productoId} asociado a la linea no encontrado" }
            }
            it.toModel(producto!!.toModel())
        }
        // Mapeamos la venta
        return ventaEntity.toModel(cliente.toModel(), lineasVenta)
    }
    
    override fun save(t: Venta): Venta {
        logger.debug { "Guardando venta: $t" }
        // Mapeamos la venta a entidad
        val ventaEntity = t.toEntity()
        // Guardamos la venta
        println(ventaEntity)
        daoVentas.save(ventaEntity)
        logger.debug { "Venta guardada: $ventaEntity" }
        // Guardamos las lineas de venta
        t.lineas.forEach {
            logger.debug { "Guardando linea de venta: $it" }
            val lineaEntity = it.toEntity(ventaEntity.id)
            daoLineas.save(lineaEntity)
            logger.debug { "Linea de venta guardada: $lineaEntity" }
        }
        return t
    }
    
    override fun update(id: UUID, t: Venta): Venta? {
        TODO("Not yet implemented")
    }
    
    override fun delete(id: UUID): Venta? {
        TODO("Not yet implemented")
    }
    
}