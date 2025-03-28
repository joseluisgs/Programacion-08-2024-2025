package dev.joseluisgs.ventas.service

import com.github.michaelbull.result.Result
import dev.joseluisgs.clientes.model.Cliente
import dev.joseluisgs.ventas.error.VentaError
import dev.joseluisgs.ventas.models.LineaVenta
import dev.joseluisgs.ventas.models.Venta
import java.util.*

interface VentasService {
    fun getById(id: UUID): Result<Venta, VentaError>
    fun create(venta: Venta): Result<Venta, VentaError>
    fun create(cliente: Cliente, lineas: List<LineaVenta>): Result<Venta, VentaError>
    fun exportToJson(venta: Venta, jsonFile: String): Result<Unit, VentaError>
    fun exportToHtml(venta: Venta, htmlFile: String): Result<Unit, VentaError>
}