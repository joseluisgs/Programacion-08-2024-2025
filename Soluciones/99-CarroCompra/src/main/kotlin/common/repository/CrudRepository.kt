package dev.joseluisgs.common.repository

interface CrudRepository<T, ID> {
    fun findAll(): List<T>
    fun findById(id: ID): T?
    fun save(t: T): T
    fun update(id: ID, t: T): T?
    fun delete(id: ID): T?
}