package dev.joseluisgs.ventas.service

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.andThen
import dev.joseluisgs.clientes.model.Cliente
import dev.joseluisgs.clientes.repository.ClientesRepository
import dev.joseluisgs.productos.repository.ProductosRepository
import dev.joseluisgs.ventas.error.VentaError
import dev.joseluisgs.ventas.models.LineaVenta
import dev.joseluisgs.ventas.models.Venta
import dev.joseluisgs.ventas.repository.VentasRepository
import dev.joseluisgs.ventas.storage.VentasStorage
import org.koin.core.annotation.Named
import org.koin.core.annotation.Singleton
import org.lighthousegames.logging.logging
import java.util.*

@Singleton
class VentasServiceImpl(
    private val ventasRepository: VentasRepository,
    private val productosRepository: ProductosRepository,
    private val clientesRepository: ClientesRepository,
    @Named("VentasStorageJson")
    private val ventasStorageJson: VentasStorage,
    @Named("VentasStorageHtml")
    private val ventasStorageHtml: VentasStorage
) : VentasService {
    
    private val logger = logging()
    override fun getById(id: UUID): Result<Venta, VentaError> {
        logger.debug { "Obteniendo venta por id: $id" }
        return ventasRepository.findById(id)
            ?.let { Ok(it) }
            ?: Err(VentaError.VentaNoEncontrada(id))
        
        
    }
    
    override fun create(venta: Venta): Result<Venta, VentaError> {
        logger.debug { "Creando venta: $venta" }
        
        // Validamos la vennta
        // ROP
        return validateCliente(venta.cliente)
            .andThen { validateLineas(venta.lineas) }
            .andThen { actualizarStock(venta.lineas) }
            .andThen { Ok(ventasRepository.save(venta)) }
    }
    
    override fun create(cliente: Cliente, lineas: List<LineaVenta>): Result<Venta, VentaError> {
        logger.debug { "Creando venta con cliente y lineas: $cliente, $lineas" }
        
        // Validamos la vennta
        // ROP
        return validateCliente(cliente)
            .andThen { validateLineas(lineas) }
            .andThen { actualizarStock(lineas) }
            .andThen { Ok(ventasRepository.save(Venta(cliente = cliente, lineas = lineas))) }
    }
    
    
    private fun validateCliente(cliente: Cliente): Result<Cliente, VentaError> {
        // Hay que validar que el cliente exista en el repositorio de clientes
        logger.debug { "Validando cliente: $cliente" }
        return clientesRepository.findById(cliente.id)
            ?.let { Ok(it) }
            ?: Err(VentaError.VentaNoValida("Cliente no encontrado con id: ${cliente.id}"))
    }
    
    private fun validateLineas(lineas: List<LineaVenta>): Result<List<LineaVenta>, VentaError> {
        // Hay que validar que los productos existan en el repositorio de productos
        logger.debug { "Validando lineas - Existen Productos: $lineas" }
        lineas.forEach {
            productosRepository.findById(it.producto.id)
                ?: return Err(VentaError.VentaNoValida("Producto no encontrado con id: ${it.producto.id}"))
        }
        // Debemos validar que la cantidad de productos sea mayor que 0 y menor que el stock
        logger.debug { "Validando lineas - Cantidad y Stock de productos: $lineas" }
        lineas.forEach {
            if (it.cantidad <= 0) {
                return Err(VentaError.VentaNoValida("La cantidad de productos debe ser mayor que 0"))
            }
            productosRepository.findById(it.producto.id)?.let { producto ->
                if (it.cantidad > producto.stock) {
                    return Err(VentaError.VentaNoValida("No hay suficiente stock para el producto: ${producto.nombre}, stock: ${producto.stock} cantidad: ${it.cantidad}"))
                }
            }
        }
        return Ok(lineas)
    }
    
    private fun actualizarStock(lineas: List<LineaVenta>): Result<List<LineaVenta>, VentaError> {
        logger.debug { "Actualizando stock de productos: $lineas" }
        lineas.forEach {
            productosRepository.findById(it.producto.id)?.let { producto ->
                val updateProducto = producto.copy(stock = producto.stock - it.cantidad)
                productosRepository.update(producto.id, updateProducto)
                    ?: return Err(VentaError.VentaNoValida("Error al actualizar el stock del producto: ${producto.nombre}"))
            }
        }
        return Ok(lineas)
    }
    
    override fun exportToJson(venta: Venta, jsonFile: String): Result<Unit, VentaError> {
        logger.debug { "Exportando venta a fichero csv: $jsonFile" }
        return ventasStorageJson.export(venta, jsonFile)
    }
    
    override fun exportToHtml(venta: Venta, htmlFile: String): Result<Unit, VentaError> {
        logger.debug { "Exportando venta a fichero html: $htmlFile" }
        return ventasStorageHtml.export(venta, htmlFile)
    }
}