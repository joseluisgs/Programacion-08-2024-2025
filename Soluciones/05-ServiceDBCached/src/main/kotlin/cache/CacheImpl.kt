package dev.joseluisgs.cache

import org.lighthousegames.logging.logging

/**
 * Implementación de una cache LRU
 * @param K Tipo de la clave
 * @param V Tipo del valor
 * @property capacity Capacidad de la cache
 * @property type Tipo de cache (LRU o FIFO)
 *
 */
class CacheImpl<K, V>(private val capacity: Int, private val type: CacheType) : Cache<K, V> {
    private val logger = logging()
    
    // El truco de LinkedHashMap es que podemos sobreescribir el método removeEldestEntry
    // para que nos devuelva true si queremos eliminar el elemento más antiguo
    // con lo que conseguimos una cache LRU gracias a accesos y actualizaciones
    // según nuestro criterio. En este caso, hemos establecido un factor de carga
    // accessOrder a true, para que se ordene por accesos y no por inserción.
    private val cache = object : LinkedHashMap<K, V>(
        capacity, 0.75f,
        when (type) {
            CacheType.LRU -> true
            CacheType.FIFO -> false
        }
    ) {
        override fun removeEldestEntry(eldest: MutableMap.MutableEntry<K, V>?): Boolean {
            logger.debug { "Alcanzado el tamaño máximo: eliminando elemento más antiguo de la cache" }
            return size > capacity
        }
    }
    
    init {
        logger.debug { "Creando cache con capacidad $capacity y tipo $type" }
    }
    
    override fun get(key: K): V? {
        logger.debug { "Obteniendo elemento de la cache con clave $key" }
        return cache[key]
    }
    
    override fun put(key: K, value: V): V? {
        logger.debug { "Guardando elemento en la cache con clave $key" }
        // Cuidado con el put que devuelve el valor anterior
        cache[key] = value
        return value
    }
    
    override fun remove(key: K): V? {
        logger.debug { "Borrando elemento de la cache con clave $key" }
        return cache.remove(key)
    }
    
    override fun clear() {
        logger.debug { "Limpiando la cache" }
        cache.clear()
    }
    
    override fun size(): Int {
        logger.debug { "Obteniendo tamaño de la cache" }
        return cache.size
    }
    
    override fun keys(): Set<K> {
        logger.debug { "Obteniendo claves de la cache" }
        return cache.keys
    }
    
    override fun values(): Collection<V> {
        logger.debug { "Obteniendo valores de la cache" }
        return cache.values
    }
    
    override fun entries(): Set<Map.Entry<K, V>> {
        logger.debug { "Obteniendo entradas de la cache" }
        return cache.entries
    }
    
    enum class CacheType {
        LRU, FIFO
    }
}