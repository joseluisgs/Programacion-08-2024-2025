package dev.joseluisgs.clientes.repository

import dev.joseluisgs.clientes.model.Cliente
import dev.joseluisgs.common.repository.CrudRepository

interface ClientesRepository : CrudRepository<Cliente, Long> {
    fun deleteLogico(id: Long): Cliente?
}
