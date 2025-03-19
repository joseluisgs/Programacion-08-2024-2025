package dev.joseluisgs.database

interface DatabaseManager {
    fun select(query: String, vararg params: Any): List<Map<String, Any>>
    fun insert(query: String, vararg params: Any): Int
    fun insertAndGetId(query: String, vararg params: Any): Any
    fun update(query: String, vararg params: Any): Int
    fun delete(query: String, vararg params: Any): Int
}
