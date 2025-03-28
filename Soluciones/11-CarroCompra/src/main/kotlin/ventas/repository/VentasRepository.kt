package dev.joseluisgs.ventas.repository

import dev.joseluisgs.common.repository.CrudRepository
import dev.joseluisgs.ventas.models.Venta
import java.util.*

interface VentasRepository : CrudRepository<Venta, UUID> {
    //fun findLineasByVentaId(id: UUID): List<LineaVenta>
}