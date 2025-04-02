package dev.joseluisgs.clientes.service

import com.github.michaelbull.result.Result
import dev.joseluisgs.clientes.error.ClienteError
import dev.joseluisgs.clientes.model.Cliente

interface ClientesService {
    fun getAll(): Result<List<Cliente>, ClienteError>
    fun getById(id: Long): Result<Cliente, ClienteError>
    fun create(cliente: Cliente): Result<Cliente, ClienteError>
    fun update(id: Long, cliente: Cliente): Result<Cliente, ClienteError>
    fun delete(id: Long): Result<Cliente, ClienteError>
}